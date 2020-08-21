package com.apon.javadocservlet.zip;

import com.apon.javadocservlet.JavadocServletApplication;
import com.apon.javadocservlet.controllers.ApplicationException;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ZipCache {
    private final ArtifactStorage artifactStorage;
    private final LoadingCache<Artifact, Map<String, byte[]>> cachedZipContent = CacheBuilder.newBuilder()
            .maximumWeight(JavadocServletApplication.MAX_STORAGE_SIZE_ZIP_FILES_IN_BYTES)
            // The weight of an element is the sum of the length of all bytes.
            .weigher((Weigher<Artifact, Map<String, byte[]>>) (key, value) -> value.values().stream().mapToInt(file -> file.length).sum())
            .build(new CacheLoader<>() {
                @Override
                public Map<String, byte[]> load(@SuppressWarnings("NullableProblems") Artifact artifact) throws IOException, ExecutionException, ArtifactSearchException {
                    byte[] zip = searchZipFile(artifact);
                    zipChecksum.get(artifact, () -> ZipDetailsCreator.determineChecksum(zip));
                    return ZipDetailsCreator.determineContentAsMap(zip);
                }
            });

    private final LoadingCache<Artifact, String> zipChecksum = CacheBuilder.newBuilder()
            .maximumSize(JavadocServletApplication.MAX_NUMBER_OF_ZIP_CHECKSUMS_IN_CACHE)
            .build(new CacheLoader<>() {
                @Override
                public String load(@SuppressWarnings("NullableProblems") Artifact artifact) throws ArtifactSearchException {
                    // Entries are also loaded when cachedZipContent is updated. However, if we are trying to search
                    // this cache without an entry present, search through artifact storage anyway.
                    byte[] zip = searchZipFile(artifact);
                    return ZipDetailsCreator.determineChecksum(zip);
                }
            });

    public ZipCache(ArtifactStorage artifactStorage) {
        this.artifactStorage = artifactStorage;
    }

    private byte[] searchZipFile(Artifact artifact) throws ArtifactSearchException {
        return artifactStorage.getJavaDocJar(artifact);
    }

    /**
     * Returns the content of a file inside a zip.
     * @param artifact The artifact
     * @param filePath The relative path of the file inside the zip
     */
    public Optional<byte[]> getContentOfFileFromZip(Artifact artifact, String filePath) {
        try {
            Map<String, byte[]> zipContent = cachedZipContent.get(artifact);

            if (!zipContent.containsKey(filePath)) {
                return Optional.empty();
            }

            return Optional.of(zipContent.get(filePath));
        } catch (ExecutionException e) {
            throw new ApplicationException(e);
        }
    }

    /**
     * @param artifact The artifact
     * @return The checksum of the javadoc.jar corresponding to the artifact.
     */
    public String getChecksum(Artifact artifact) {
        try {
            return zipChecksum.get(artifact);
        } catch (ExecutionException e) {
            throw new ApplicationException(e);
        }
    }
}
