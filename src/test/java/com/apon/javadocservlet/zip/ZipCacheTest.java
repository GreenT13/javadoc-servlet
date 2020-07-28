package com.apon.javadocservlet.zip;

import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.apon.javadocservlet.zip.ByteMatcher.matchesString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class ZipCacheTest {
    private static final byte[] file = TestZipConstants.FILE;

    @Test
    public void happyFlow() throws ArtifactSearchException, ExecutionException {
        // Given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String version = "version";

        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doReturn(file).when(artifactStorage).getJavaDocJar(groupId, artifactId, version);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When
        byte[] file = zipCache.getContentOfFileFromZip(groupId, artifactId, version, TestZipConstants.FILE_PATH_1).get();

        // Then
        assertThat(file, matchesString(TestZipConstants.FILE_CONTENT_1));
    }

    @Test
    public void throwExceptionWhenZipCouldNotBeFound() throws ArtifactSearchException {
        // Given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String version = "version";
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doThrow(ArtifactSearchException.class).when(artifactStorage).getJavaDocJar(groupId, artifactId, version);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When and then
        assertThrows(ExecutionException.class, () -> zipCache.getContentOfFileFromZip(groupId, artifactId, version, "irrelevant"));
    }

    @Test
    public void returnEmptyIfFileCouldNotBeFound() throws ArtifactSearchException, ExecutionException {
        // Given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String version = "version";

        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        doReturn(file).when(artifactStorage).getJavaDocJar(groupId, artifactId, version);
        ZipCache zipCache = new ZipCache(artifactStorage);

        // When
        Optional<byte[]> file = zipCache.getContentOfFileFromZip(groupId, artifactId, version, "non-existent-path");

        // Then
        assertThat("File should not be found", file.isEmpty(), equalTo(true));
    }

    @Test
    public void twoArtifactKeysWithIdenticalContentAreEqual() {
        // Given
        String groupId = "groupId";
        String artifactId = "artifactId";
        String version = "version";

        // When
        ZipCache.ArtifactKey artifactKey1 = new ZipCache.ArtifactKey(groupId, artifactId, version);
        ZipCache.ArtifactKey artifactKey2 = new ZipCache.ArtifactKey(groupId, artifactId, version);

        // Then
        assertThat(artifactKey1, equalTo(artifactKey2));
        assertThat(artifactKey1.hashCode(), equalTo(artifactKey2.hashCode()));
    }
}
