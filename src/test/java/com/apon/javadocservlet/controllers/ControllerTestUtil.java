package com.apon.javadocservlet.controllers;

import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Optional;

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
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        httpServletRequest.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, path);

        // ServletWebRequest#getRequest() is final so we cannot mock it. Creating the object itself solves the issue.
        ServletWebRequest webRequest = spy(new ServletWebRequest(httpServletRequest));
        doReturn(false).when(webRequest).checkNotModified(anyString());

        return webRequest;
    }

    public static ZipCache createZipCache(byte[] file, String checksum) {
        ZipCache zipCache = mock(ZipCache.class);
        doReturn(Optional.ofNullable(file)).when(zipCache).getContentOfFileFromZip(any(), anyString());
        doReturn(checksum).when(zipCache).getChecksum(any());

        return zipCache;
    }
}
