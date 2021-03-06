package com.apon.javadocservlet.zip;

import com.apon.javadocservlet.controllers.ApplicationException;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.apon.javadocservlet.zip.ByteMatcher.matchesString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class ZipCacheTest {
    @Test
    void happyFlow() throws ArtifactSearchException {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "version");
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doReturn(TestZipConstants.FILE).when(artifactStorage).getJavaDocJar(artifact);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When
        byte[] file = zipCache.getContentOfFileFromZip(artifact, TestZipConstants.FILE_PATH_1).get();

        // Then
        assertThat(file, matchesString(TestZipConstants.FILE_CONTENT_1));
    }

    @Test
    void throwExceptionWhenZipCouldNotBeFound() throws ArtifactSearchException {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "version");
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doThrow(ArtifactSearchException.class).when(artifactStorage).getJavaDocJar(artifact);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When and then
        assertThrows(ApplicationException.class, () -> zipCache.getContentOfFileFromZip(artifact, "irrelevant"));
    }

    @Test
    void returnEmptyIfFileCouldNotBeFound() throws ArtifactSearchException {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "version");
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doReturn(TestZipConstants.FILE).when(artifactStorage).getJavaDocJar(artifact);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When
        Optional<byte[]> file = zipCache.getContentOfFileFromZip(artifact, "non-existent-path");

        // Then
        assertThat("File should not be found", file.isEmpty(), equalTo(true));
    }

    @Test
    void checksumCacheIsFilledAfterRetrievingFile() throws ArtifactSearchException {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "version");
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doReturn(TestZipConstants.FILE).when(artifactStorage).getJavaDocJar(artifact);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When
        zipCache.getContentOfFileFromZip(artifact, "non-existent-path");
        zipCache.getChecksum(artifact);

        // Then
        verify(artifactStorage).getJavaDocJar(artifact);
    }

    @Test
    void correctChecksumIsRetrieved() throws ArtifactSearchException {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "version");
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doReturn(TestZipConstants.FILE).when(artifactStorage).getJavaDocJar(artifact);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When
        String checksum = zipCache.getChecksum(artifact);

        // Then
        assertThat(checksum, equalTo(TestZipConstants.FILE_CHECKSUM));
    }

    @Test
    void checksumThrowsErrorWhenCacheThrowsException() throws ArtifactSearchException {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "version");
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doThrow(ArtifactSearchException.class).when(artifactStorage).getJavaDocJar(artifact);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When and then
        assertThrows(ApplicationException.class, () -> zipCache.getChecksum(artifact));
    }
}
