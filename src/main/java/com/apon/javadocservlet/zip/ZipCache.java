package com.apon.javadocservlet.zip;

import com.apon.javadocservlet.JavadocServletApplication;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.Weigher;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ZipCache {
    private final ArtifactStorage artifactStorage;
    private final LoadingCache<ArtifactKey, Map<String, byte[]>> cachedZipContent = CacheBuilder.newBuilder()
            .maximumWeight(JavadocServletApplication.MAX_STORAGE_SIZE_ZIP_FILES_IN_BYTES)
            // The weight of an element is the sum of the length of all bytes.
            .weigher((Weigher<ArtifactKey, Map<String, byte[]>>) (key, value) -> value.values().stream().mapToInt(file -> file.length).sum())
            .build(new CacheLoader<>() {
                @Override
                public Map<String, byte[]> load(@SuppressWarnings("NullableProblems") ArtifactKey artifactKey) throws IOException, ExecutionException, ArtifactSearchException {
                    byte[] zip = searchZipFile(artifactKey);
                    cachedMd5SumZip.get(artifactKey, () -> ZipDetailsCreator.determineMd5Hash(zip));
                    return ZipDetailsCreator.determineContentAsMap(zip);
                }
            });

    private final LoadingCache<ArtifactKey, String> cachedMd5SumZip = CacheBuilder.newBuilder()
            .maximumSize(JavadocServletApplication.MAX_NUMBER_OF_ZIP_HASHES_IN_CACHE)
            .build(new CacheLoader<>() {
                @Override
                public String load(@SuppressWarnings("NullableProblems") ArtifactKey artifactKey) throws ArtifactSearchException {
                    // Entries are also loaded when cachedZipContent is updated. However, if we are trying to search
                    // this cache without an entry present, search through artifact storage anyway.
                    byte[] zip = searchZipFile(artifactKey);
                    return ZipDetailsCreator.determineMd5Hash(zip);
                }
            });

    public ZipCache(ArtifactStorage artifactStorage) {
        this.artifactStorage = artifactStorage;
    }

    private byte[] searchZipFile(ArtifactKey artifactKey) throws ArtifactSearchException {
        return artifactStorage.getJavaDocJar(artifactKey.getGroupId(), artifactKey.getArtifactId(), artifactKey.getVersion());
    }

    /**
     * Returns the content of a file inside a zip.
     * @param groupId    The groupId
     * @param artifactId The artifactId
     * @param version    The version
     * @param filePath   The relative path of the file inside the zip
     */
    // TODO: accept Artifact object?
    public Optional<byte[]> getContentOfFileFromZip(String groupId, String artifactId, String version, String filePath) throws ExecutionException {
        ArtifactKey artifactKey = new ArtifactKey(groupId, artifactId, version);
        Map<String, byte[]> zipContent = cachedZipContent.get(artifactKey);

        if (!zipContent.containsKey(filePath)) {
            return Optional.empty();
        }

        return Optional.of(zipContent.get(filePath));
    }

    public String getMd5HashFromZip(String groupId, String artifactId, String version) throws ExecutionException {
        ArtifactKey artifactKey = new ArtifactKey(groupId, artifactId, version);
        return cachedMd5SumZip.get(artifactKey);
    }

    /**
     * Immutable object representing the combination of groupId, artifactId and version of an artifact.
     */
    // TODO: replace this with artifact?
    static class ArtifactKey {
        private final String groupId;
        private final String artifactId;
        private final String version;

        public ArtifactKey(String groupId, String artifactId, String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ArtifactKey that = (ArtifactKey) o;
            return Objects.equals(groupId, that.groupId) &&
                    Objects.equals(artifactId, that.artifactId) &&
                    Objects.equals(version, that.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(groupId, artifactId, version);
        }

        public String getGroupId() {
            return groupId;
        }

        public String getArtifactId() {
            return artifactId;
        }

        public String getVersion() {
            return version;
        }
    }
}
