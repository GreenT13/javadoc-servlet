package com.apon.javadocservlet.repository;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ArtifactTest {

    @Test
    public void equalsAndHashCodeAreImplemented() {
        // Given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String latestVersion = "latestVersion";

        // When
        Artifact artifact1 = new Artifact(groupId, artifactId, latestVersion);
        Artifact artifact2 = new Artifact(groupId, artifactId, latestVersion);

        // Then
        assertThat(artifact1, equalTo(artifact2));
        assertThat(artifact1.hashCode(), equalTo(artifact2.hashCode()));
    }
}
