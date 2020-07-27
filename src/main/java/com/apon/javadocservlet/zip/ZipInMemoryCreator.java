package com.apon.javadocservlet.zip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipInMemoryCreator {

    public Map<String, byte[]> getContentAsMap(byte[] file) throws IOException {
        return getContentAsMap(new ByteArrayInputStream(file));
    }

    /**
     * Returns a map with the full content of the zip with the relative file path as key.
     * This method does not close the input stream.
     * @param inputStream The input stream.
     */
    public Map<String, byte[]> getContentAsMap(InputStream inputStream) throws IOException {
        return getContentAsMap(new ZipInputStream(inputStream));
    }

    /**
     * Returns a map with the full content of the zip with the relative file path as key.
     * This method does not close the input stream.
     * @param zipInputStream The input stream.
     */
    public Map<String, byte[]> getContentAsMap(ZipInputStream zipInputStream) throws IOException {
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
