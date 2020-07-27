package com.apon.javadocservlet.controllers;

import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;

public class ControllerUtil {
    /**
     * Get the called URL where the prefix is removed.
     * @param httpServletRequest The request
     * @param prefix The prefix. Make sure you add the starting and ending slash.
     * @return The relative called URL
     */
    public static String getRelativeUrl(HttpServletRequest httpServletRequest, String prefix) {
        String requestedUri = (String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return requestedUri.substring(prefix.length());
    }
}
