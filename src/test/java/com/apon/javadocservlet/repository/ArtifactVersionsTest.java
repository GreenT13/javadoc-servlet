package com.apon.javadocservlet.repository;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArtifactVersionsTest {

    @Test
    public void equalsAndHashCodeAreImplemented() {
        EqualsVerifier.simple().forClass(ArtifactVersions.class).verify();
        EqualsVerifier.simple().forClass(ArtifactVersions.Version.class).verify();
    }

    @Test
    public void artifactVersionIsImmutable() {
        // Given
        ArtifactVersions.Version version1 = new ArtifactVersions.Version("version1", true);
        List<ArtifactVersions.Version> versions = new ArrayList<>(Collections.singletonList(version1));
        ArtifactVersions artifactVersions = new ArtifactVersions(versions);

        // Modifying input list doesn't change object.
        versions.add(new ArtifactVersions.Version("version2", true));
        assertThat(artifactVersions.getVersions(), contains(version1));

        // Modifying list from getter is not possible.
        assertThrows(UnsupportedOperationException.class, () ->
                artifactVersions.getVersions().add(new ArtifactVersions.Version("version2", true)));
    }

    @Test
    public void toStringMethodIsImplemented() {
        // Given
        ArtifactVersions.Version version = new ArtifactVersions.Version("version1", true);

        // When and then
        assertThat(version, hasToString(allOf(containsString("version1"), containsString("true"))));
    }
}
