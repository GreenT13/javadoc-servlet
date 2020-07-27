package com.apon.javadocservlet.repository;

import java.util.Collections;
import java.util.List;

/**
 * The information of a single {@link Artifact} with a list of all the versions, including the specification whether
 * each version contains a javadoc.jar or not.
 */
public class ArtifactVersions {
    private final Artifact artifact;
    private final List<Version> versions;

    public ArtifactVersions(Artifact artifact, List<Version> versions) {
        this.artifact = artifact;
        this.versions = Collections.unmodifiableList(versions);
    }

    public static class Version {
        private final String version;
        private final boolean hasJavaDocJar;

        public Version(String version, boolean hasJavaDocJar) {
            this.version = version;
            this.hasJavaDocJar = hasJavaDocJar;
        }

        public String getVersion() {
            return version;
        }

        public boolean isHasJavaDocJar() {
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
