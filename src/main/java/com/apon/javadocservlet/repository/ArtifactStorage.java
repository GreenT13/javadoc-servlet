package com.apon.javadocservlet.repository;

import java.util.List;

/**
 * Interface representing any kind of storage which contains deployed artifacts. This can be anything like Maven Central,
 * Keylane Artifactory or just a local .m2 directory.
 */
public interface ArtifactStorage {
    List<Artifact> findArtifacts(String groupId, String artifactId) throws ArtifactSearchException;

    ArtifactVersions findArtifactVersions(Artifact artifact) throws ArtifactSearchException;

    byte[] getJavaDocJar(String groupId, String artifactId, String version) throws ArtifactSearchException;
}
