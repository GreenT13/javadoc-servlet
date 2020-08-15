package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.JavadocServletApplication;
import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.http.HttpHeaders;
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

        // Check the request against the etag (checksum of the zip). If it matches, no content has been changed.
        String ifNoneMatch = webRequest.getHeader(HttpHeaders.IF_NONE_MATCH);
        if (ifNoneMatch != null) {
            String etag = zipCache.getChecksum(artifact);
            if (ifNoneMatch.equals(etag)) {
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED)
                        .cacheControl(JavadocServletApplication.CACHE_CONTROL)
                        .eTag(etag)
                        .build();
            }
        }

        Optional<byte[]> optionalFileContent = zipCache.getContentOfFileFromZip(artifact, filePath);

        // Determine the etag after retrieving the content of the file. This way, we don't retrieve the artifact twice.
        String etag = zipCache.getChecksum(artifact);

        if (optionalFileContent.isEmpty()) {
            // Return 200 also with cache control headers, so that this request will also not be called again.
            // If we would sent a 404, browsers will not send the if-none-match header, meaning we will need
            // to read the javadoc jar into memory again. To avoid this mechanic, we return an HTTP 200.
            // Very unconventional, but this is because javadoc tries to retrieve files that do not exist.
            return ResponseEntity.status(HttpStatus.OK)
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
