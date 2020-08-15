package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.JavadocServletApplication;
import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Controller
public class ApiDocController {
    public final static String API_DOC_URL = "/apidoc/";

    private final ZipCache zipCache;
    private final UrlUtil urlUtil;

    public ApiDocController(ZipCache zipCache, UrlUtil urlUtil) {
        this.zipCache = zipCache;
        this.urlUtil = urlUtil;
    }

    @GetMapping(API_DOC_URL + "**")
    @ResponseBody
    public ResponseEntity<byte[]> getFileInZip(WebRequest webRequest) throws ExecutionException {
        // Note: all responses must always use the cache control headers, even the 304 request.
        // So make sure to always use the CACHE_CONTROL and etag when building the ResponseEntity.

        Artifact artifact = urlUtil.createArtifactFromUrl(webRequest, API_DOC_URL);
        String filePath = urlUtil.getFilePathFromUrl(webRequest, API_DOC_URL);

        // Check the request against the etag (MD5 hash of the zip). If it matches, no content has been changed.
        String etag = zipCache.getChecksum(artifact);
        if (webRequest.checkNotModified(etag)) {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                    .cacheControl(JavadocServletApplication.CACHE_CONTROL)
                    .eTag(etag)
                    .build();
        }

        Optional<byte[]> optionalFileContent = zipCache.getContentOfFileFromZip(artifact, filePath);
        if (optionalFileContent.isEmpty()) {
            // Return 404 also with cache control headers, so that this request will also not be called again.
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .cacheControl(JavadocServletApplication.CACHE_CONTROL)
                    .eTag(etag)
                    .build();
        }

        return ResponseEntity.status(HttpStatus.OK)
                .cacheControl(JavadocServletApplication.CACHE_CONTROL)
                .eTag(etag)
                .body(optionalFileContent.get());
    }
}
