package com.apon.javadocservlet.controllers.apidoc;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        List<String> etags = item.getHeaders().get("ETag");
        if (etags == null || etags.size() != 1) {
            return false;
        }
        String etag = etags.get(0);

        // Spring automatically adds quotes around the etag.
        if (!etag.equals("\"" + correctEtag + "\"")) {
            return false;
        }

        // Don't check the content of the cache control. Having the header is enough.
        return getCacheControl(item).isPresent();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("etag \"").appendText(correctEtag).appendText("\" with Cache-Control header");
    }

    private Optional<String> getEtag(ResponseEntity<?> item) {
        List<String> etags = item.getHeaders().get("ETag");
        if (etags == null || etags.size() != 1) {
            return Optional.empty();
        }

        return Optional.of(etags.get(0));
    }

    private Optional<String> getCacheControl(ResponseEntity<?> item) {
        List<String> cacheControl = item.getHeaders().get("Cache-Control");
        if (cacheControl == null || cacheControl.size() == 0) {
            return Optional.empty();
        }

        return Optional.of(String.join(",", cacheControl));
    }

    @Override
    protected void describeMismatchSafely(ResponseEntity<?> item, Description mismatchDescription) {
        getEtag(item).ifPresentOrElse(
            (etag) -> mismatchDescription.appendText("etag ").appendText(etag),
            () -> mismatchDescription.appendText("no etag supplied"));

        getCacheControl(item).ifPresentOrElse(
            (cacheControl) -> mismatchDescription.appendText(" and Cache-Control header ").appendText(cacheControl),
            () -> mismatchDescription.appendText(" and no Cache-Control header")
        );
    }
}
