package com.apon.javadocservlet.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Class for creating {@link Map} representations of a zip file.
 */
public class ZipInMemoryCreator {

    /**
     * Returns a map with the full content of the zip with the relative file path as key.
     * @param file The file content
     */
    public Map<String, byte[]> getContentAsMap(byte[] file) throws IOException {
        Objects.requireNonNull(file);

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(file))) {
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
}
