package com.apon.javadocservlet.zip;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class TestZipConstants {
    final static String TEST_ZIP_LOCATION = "test.zip";

    final static String FILE_PATH_1 = "file1.txt";
    final static String FILE_PATH_2 = "subdir/file2.txt";

    final static String FILE_CONTENT_1 = "Content of file1.txt";
    final static String FILE_CONTENT_2 = "Content of file2.txt";
    static byte[] FILE;

    static {
        try {
            FILE = TestZipConstants.class.getResourceAsStream(TEST_ZIP_LOCATION).readAllBytes();

            if (FILE == null) {
                fail("Could not find resource for test");
            }
        } catch (IOException e) {
            fail(e);
        }
    }
}
