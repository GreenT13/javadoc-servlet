package com.apon.javadocservlet.controllers.apidoc;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

public class CacheControlMatcher extends TypeSafeMatcher<ResponseEntity<?>> {
    private final String correctEtag;

    public CacheControlMatcher(String correctEtag) {
        this.correctEtag = correctEtag;
    }

    public static CacheControlMatcher hasCacheControlWithEtag(String etag) {
        return new CacheControlMatcher(etag);
    }

    @Override
    protected boolean matchesSafely(ResponseEntity<?> item) {
        Objects.requireNonNull(item);
        String etag = getEtag(item);

        // Spring automatically adds quotes around the etag.
        if (!etag.equals("\"" + correctEtag + "\"")) {
            return false;
        }

        // Don't check the content of the cache control. Having the header is enough.
        return getCacheControl(item) != null;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("etag \"").appendText(correctEtag).appendText("\" with Cache-Control header");
    }

    @SuppressFBWarnings(justification = "SpotBugs complains about possibly returning null, but NPE just fails the test.")
    private String getEtag(ResponseEntity<?> item) {
        return Objects.requireNonNull(item.getHeaders().get("ETag")).get(0);
    }

    @SuppressFBWarnings(justification = "SpotBugs complains about possibly returning null, but NPE just fails the test.")
    private String getCacheControl(ResponseEntity<?> item) {
        return Objects.requireNonNull(item.getHeaders().get("Cache-Control")).get(0);
    }

    @Override
    protected void describeMismatchSafely(ResponseEntity<?> item, Description mismatchDescription) {
        mismatchDescription.appendText("etag ").appendText(getEtag(item)).appendText(" and Cache-Control header ").appendText(getCacheControl(item));
    }
}
