package com.apon.javadocservlet.repository;

import java.util.List;

/**
 * Interface representing any kind of storage which contains deployed artifacts. This can be anything like Maven Central,
 * Keylane Artifactory or just a local .m2 directory.
 */
public interface ArtifactStorage {
    /**
     * Find the artifacts for the given groupId and artifactId. The {@link Artifact#getVersion()} will be filled
     * with the latest version available.
     * @param groupId    The groupId.
     * @param artifactId The artifactId.
     * @return List of {@link Artifact} objects.
     * @throws ArtifactSearchException If something goes wrong.
     */
    List<Artifact> findArtifacts(String groupId, String artifactId) throws ArtifactSearchException;

    /**
     * Find all versions for a specific artifact.
     * @param artifact The artifact
     * @return {@link ArtifactVersions}
     * @throws ArtifactSearchException If something goes wrong.
     */
    ArtifactVersions findArtifactVersions(Artifact artifact) throws ArtifactSearchException;

    byte[] getJavaDocJar(Artifact artifact) throws ArtifactSearchException;
}
