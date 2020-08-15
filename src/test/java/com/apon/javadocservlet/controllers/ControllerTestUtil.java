package com.apon.javadocservlet.controllers;

import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class ControllerTestUtil {
    public static UrlUtil createUrlUtil(String contextPath) {
        MockServletContext mockServletContext = new MockServletContext();
        mockServletContext.setContextPath(contextPath);
        return new UrlUtil(mockServletContext);
    }

    public static UrlUtil createUrlUtil() {
        return createUrlUtil("");
    }

    public static WebRequest createWebRequest(String path) {
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        doReturn(path).when(httpServletRequest).getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        // ServletWebRequest#getRequest() is final so we cannot mock it. Creating the object itself solves the issue.
        ServletWebRequest webRequest = spy(new ServletWebRequest(httpServletRequest));
        doReturn(false).when(webRequest).checkNotModified(anyString());

        return webRequest;
    }

    public static ZipCache createZipCache(byte[] file, String checksum) {
        ZipCache zipCache = mock(ZipCache.class);
        try {
            doReturn(Optional.ofNullable(file)).when(zipCache).getContentOfFileFromZip(any(), anyString());
        } catch (ExecutionException e) {
            fail(e);
        }
        try {
            doReturn(checksum).when(zipCache).getChecksum(any());
        } catch (ExecutionException e) {
            fail(e);
        }

        return zipCache;
    }
}
