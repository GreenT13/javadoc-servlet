package com.apon.javadocservlet.zip;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.nio.charset.StandardCharsets;

public class ByteMatcher extends TypeSafeMatcher<byte[]> {
    private final String expectedText;

    public ByteMatcher(String expectedText) {
        this.expectedText = expectedText;
    }

    public static Matcher<byte[]> matchesString(String expectedText) {
        return new ByteMatcher(expectedText);
    }

    @Override
    protected boolean matchesSafely(byte[] item) {
        String actualText = convertContentToUtf8String(item);
        return expectedText.equals(actualText);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("the text '").appendText(expectedText).appendText("'");
    }

    @Override
    protected void describeMismatchSafely(byte[] item, Description mismatchDescription) {
        String actualText = convertContentToUtf8String(item);
        mismatchDescription.appendText("the text '").appendText(actualText).appendText("'");
    }

    /**
     * Converts the binary content of a file to a {@link String} representation using UTF-8 encoding.
     * @param file The content of the file
     * @return The content as {@link String}
     */
    private String convertContentToUtf8String(byte[] file) {
        return new String(file, StandardCharsets.UTF_8);
    }
}
