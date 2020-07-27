package com.apon.javadocservlet.zip;

import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class ZipCache {
    private final ArtifactStorage artifactStorage;
    private final LoadingCache<ArtifactKey, Map<String, byte[]>> cachedZipContent = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .build(new CacheLoader<>() {
                @Override
                public Map<String, byte[]> load(@SuppressWarnings("NullableProblems") ArtifactKey artifactKey) throws IOException {
                    return readContentFromZip(artifactKey);
                }
            });

    public ZipCache(ArtifactStorage artifactStorage) {
        this.artifactStorage = artifactStorage;
    }

    private Map<String, byte[]> readContentFromZip(ArtifactKey artifactKey) throws IOException {
        try {
            byte[] fileContent = artifactStorage.getJavaDocJar(artifactKey.getGroupId(), artifactKey.getArtifactId(), artifactKey.getVersion());
            ZipInMemoryCreator zipInMemoryCreator = new ZipInMemoryCreator();
            return zipInMemoryCreator.getContentAsMap(fileContent);
        } catch (ArtifactSearchException e) {
            throw new IOException(e);
        }
    }

    /**
     * Returns the content of a file inside a zip.
     * @param groupId    The groupId
     * @param artifactId The artifactId
     * @param version    The version
     * @param filePath   The relative path of the file inside the zip
     */
    public Optional<byte[]> getContentOfFileFromZip(String groupId, String artifactId, String version, String filePath) throws IOException, ExecutionException {
        ArtifactKey artifactKey = new ArtifactKey(groupId, artifactId, version);
        Map<String, byte[]> zipContent = cachedZipContent.get(artifactKey);
        if (zipContent == null) {
            throw new IOException("Could not determine zip content from " + artifactKey.toString());
        }

        if (!zipContent.containsKey(filePath)) {
            return Optional.empty();
        }

        return Optional.of(zipContent.get(filePath));
    }

    private static class ArtifactKey {
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

        @Override
        public String toString() {
            return "ArtifactKey{" +
                    "groupId='" + groupId + '\'' +
                    ", artifactId='" + artifactId + '\'' +
                    ", version='" + version + '\'' +
                    '}';
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
