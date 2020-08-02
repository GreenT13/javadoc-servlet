package com.apon.javadocservlet.controllers;

import javax.servlet.ServletContext;

import static org.mockito.Mockito.*;

public class ControllerTestUtil {
    public static UrlUtil createUrlUtil() {
        ServletContext servletContext = mock(ServletContext.class);
        doReturn("").when(servletContext).getContextPath();
        return new UrlUtil(servletContext);
    }
}
