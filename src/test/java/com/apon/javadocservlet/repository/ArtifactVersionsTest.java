package com.apon.javadocservlet.repository;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArtifactVersionsTest {

    private ArtifactVersions createArtifactVersions() {
        String groupId = "groupId";
        String artifactId = "artifactId";
        String version = "version";
        Artifact artifact = new Artifact(groupId, artifactId, version);
        ArtifactVersions.Version version1 = new ArtifactVersions.Version("version1", true);
        ArtifactVersions.Version version2 = new ArtifactVersions.Version("version2", false);

        return new ArtifactVersions(artifact, Arrays.asList(version1, version2));
    }

    @Test
    public void equalsAndHashCodeAreImplemented() {
        // When
        ArtifactVersions artifactVersions1 = createArtifactVersions();
        ArtifactVersions artifactVersions2 = createArtifactVersions();

        // Then
        assertThat(artifactVersions1, equalTo(artifactVersions2));
        assertThat(artifactVersions1.hashCode(), equalTo(artifactVersions2.hashCode()));
    }

    @Test
    public void artifactVersionIsImmutable() {
        // Given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String version = "version";
        Artifact artifact = new Artifact(groupId, artifactId, version);
        ArtifactVersions.Version version1 = new ArtifactVersions.Version("version1", true);
        List<ArtifactVersions.Version> versions = new ArrayList<>(Collections.singletonList(version1));
        ArtifactVersions artifactVersions = new ArtifactVersions(artifact, versions);

        // Modifying input list doesn't change object.
        versions.add(new ArtifactVersions.Version("version2", true));
        assertThat(artifactVersions.getVersions(), contains(version1));

        // Modifying list from getter is not possible.
        assertThrows(UnsupportedOperationException.class, () ->
                artifactVersions.getVersions().add(new ArtifactVersions.Version("version2", true)));
    }
}
