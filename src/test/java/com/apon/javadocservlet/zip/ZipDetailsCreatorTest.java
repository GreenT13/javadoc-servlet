package com.apon.javadocservlet.zip;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static com.apon.javadocservlet.zip.ByteMatcher.matchesString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

class ZipDetailsCreatorTest {
    @Test
    public void directoriesAreNotPartOfTheMap() throws IOException {
        // When
        Map<String, byte[]> zipContent = ZipDetailsCreator.determineContentAsMap(TestZipConstants.FILE);

        // Then
        assertThat(zipContent.keySet(), containsInAnyOrder(TestZipConstants.FILE_PATH_1, TestZipConstants.FILE_PATH_2));
    }

    @Test
    public void contentOfFilesIsRead() throws IOException {
        // When
        Map<String, byte[]> zipContent = ZipDetailsCreator.determineContentAsMap(TestZipConstants.FILE);

        // Then
        assertThat(zipContent.get(TestZipConstants.FILE_PATH_1), matchesString(TestZipConstants.FILE_CONTENT_1));
        assertThat(zipContent.get(TestZipConstants.FILE_PATH_2), matchesString(TestZipConstants.FILE_CONTENT_2));
    }

    @Test
    public void md5HashIsCorrectlyDetermined() {
        // When
        String md5Hash = ZipDetailsCreator.determineChecksum(TestZipConstants.FILE);

        // Then
        assertThat(md5Hash, equalTo(TestZipConstants.FILE_CHECKSUM));
    }
}
