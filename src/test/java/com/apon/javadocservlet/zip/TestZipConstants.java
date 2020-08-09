package com.apon.javadocservlet.zip;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

@SuppressFBWarnings(justification = "InputStream is incorrectly checked, see https://github.com/spotbugs/spotbugs/issues/1250.")
public class TestZipConstants {
    final static String TEST_ZIP_LOCATION = "test.zip";

    final static String FILE_PATH_1 = "file1.txt";
    final static String FILE_PATH_2 = "subdir/file2.txt";

    final static String FILE_CONTENT_1 = "Content of file1.txt";
    final static String FILE_CONTENT_2 = "Content of file2.txt";
    static byte[] FILE;

    static {
        try (InputStream inputStream = TestZipConstants.class.getResourceAsStream(TEST_ZIP_LOCATION)) {
            if (inputStream == null) {
                fail("Could not find resource for test");
            }

            FILE = inputStream.readAllBytes();
        } catch (IOException e) {
            fail(e);
        }
    }
}
