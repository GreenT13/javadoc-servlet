package com.apon.javadocservlet.zip;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class for creating {@link Map} representations and the MD5 hash of a zip file.
 */
public class ZipDetailsCreator {

    /**
     * Returns a map with the full content of the zip with the relative file path as key.
     * @param zip The file content.
     */
    public static Map<String, byte[]> determineContentAsMap(byte[] zip) throws IOException {

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zip))) {
            Map<String, byte[]> contentAsMap = new HashMap<>();

            // Get file with same path from the zip.
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (zipEntry.isDirectory()) {
                    continue;
                }
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                zipInputStream.transferTo(byteArrayOutputStream);
                contentAsMap.put(zipEntry.getName(), byteArrayOutputStream.toByteArray());
            }

            return contentAsMap;
        }
    }

    /**
     * @param zip The zip file.
     * @return The MD5 hash of the file.
     */
    public static String determineMd5Hash(byte[] zip) {
        return DigestUtils.md5Hex(zip);
    }
}
