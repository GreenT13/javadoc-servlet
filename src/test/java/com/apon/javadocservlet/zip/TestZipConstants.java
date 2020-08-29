package com.apon.javadocservlet.zip;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.fail;

@SuppressFBWarnings(justification = "InputStream is incorrectly checked, see https://github.com/spotbugs/spotbugs/issues/1250.")
public class TestZipConstants {
    static final String TEST_ZIP_LOCATION = "test.zip";

    static final String FILE_PATH_1 = "file1.txt";
    static final String FILE_PATH_2 = "subdir/file2.txt";

    static final String FILE_CONTENT_1 = "Content of file1.txt";
    static final String FILE_CONTENT_2 = "Content of file2.txt";

    /**
     * Byte content of resource test.zip, which has the following content:
     * - file1.txt (with text content "Content of file1.txt")
     * - subdir/file2.txt (with text content "Content of file2.txt")
     */
    public static byte[] FILE;
    public static final String FILE_CHECKSUM = "8fc0308268d356f2604a23a76fbbb1a1";
    public static final String FILE_CHECKSUM_WITH_QUOTES = "\"8fc0308268d356f2604a23a76fbbb1a1\"";

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
