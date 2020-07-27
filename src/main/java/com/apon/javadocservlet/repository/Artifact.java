package com.apon.javadocservlet.repository;

import java.util.Objects;

/**
 * Immutable class representing an artifact with all its versions.
 */
public class Artifact {
    private final String groupId;
    private final String artifactId;
    public final String latestVersion;

    public Artifact(String groupId, String artifactId, String latestVersion) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.latestVersion = latestVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Objects.equals(groupId, artifact.groupId) &&
                Objects.equals(artifactId, artifact.artifactId) &&
                Objects.equals(latestVersion, artifact.latestVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, artifactId, latestVersion);
    }

    public String getGroupId() {
        return groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
