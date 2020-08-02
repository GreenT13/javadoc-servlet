package com.apon.javadocservlet.repository;

import java.util.Objects;

/**
 * Immutable class representing an artifact with all its versions.
 */
public class Artifact {
    private final String groupId;
    private final String artifactId;
    public final String version;

    public Artifact(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artifact artifact = (Artifact) o;
        return Objects.equals(groupId, artifact.groupId) &&
                Objects.equals(artifactId, artifact.artifactId) &&
                Objects.equals(version, artifact.version);
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
