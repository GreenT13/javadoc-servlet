package com.apon.javadocservlet.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Immutable object containing the information of a single {@link Artifact} with a list of all the versions, including
 * the specification whether each version contains a javadoc.jar or not.
 */
public class ArtifactVersions {
    private final Artifact artifact;
    private final List<Version> versions;

    public ArtifactVersions(Artifact artifact, List<Version> versions) {
        this.artifact = artifact;
        this.versions = List.copyOf(versions);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArtifactVersions that = (ArtifactVersions) o;
        return Objects.equals(artifact, that.artifact) &&
                Objects.equals(versions, that.versions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(artifact, versions);
    }

    public static class Version {
        private final String version;
        private final boolean hasJavaDocJar;

        public Version(String version, boolean hasJavaDocJar) {
            this.version = version;
            this.hasJavaDocJar = hasJavaDocJar;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Version version1 = (Version) o;
            return hasJavaDocJar == version1.hasJavaDocJar &&
                    Objects.equals(version, version1.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(version, hasJavaDocJar);
        }

        @Override
        public String toString() {
            return "Version{" +
                    "version='" + version + '\'' +
                    ", hasJavaDocJar=" + hasJavaDocJar +
                    '}';
        }

        public String getVersion() {
            return version;
        }

        public boolean hasJavaDocJar() {
            return hasJavaDocJar;
        }
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public List<Version> getVersions() {
        return versions;
    }
}
