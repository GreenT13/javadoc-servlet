package com.apon.javadocservlet.controllers;

import com.apon.javadocservlet.controllers.apidoc.ApiDocController;
import com.apon.javadocservlet.controllers.frontend.FrontendController;
import com.apon.javadocservlet.repository.Artifact;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@SuppressWarnings("unused")
public class UrlUtil {

    private final ServletContext servletContext;

    public UrlUtil(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public String getUrlToHome() {
        return servletContext.getContextPath() + "/";
    }

    public String createUrlToArtifact(Artifact artifact, String version) {
        return servletContext.getContextPath() + FrontendController.DOC_ULR + artifact.getGroupId() + "/" + artifact.getArtifactId() + "/" + version + "/index.html";
    }

    public String createUrlToArtifact(Artifact artifact) {
        return createUrlToArtifact(artifact, artifact.getVersion());
    }

    public String createApiDocUrlToArtifact(Artifact artifact) {
        return servletContext.getContextPath() + ApiDocController.API_DOC_URL + artifact.getGroupId() + "/" + artifact.getArtifactId() + "/" + artifact.getVersion() + "/index.html";
    }

    public String createUrlTo(String path) {
        return servletContext.getContextPath() + FrontendController.DOC_ULR + path;
    }

    private String getContextPathWithoutTrailingSlash() {
        String contextPath = servletContext.getContextPath();

        // Remove the final slash if it exists, since this is already contained in the API_DOC_URL.
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }

        return contextPath;
    }

    public String getApiDocUrl() {
        return getContextPathWithoutTrailingSlash() + ApiDocController.API_DOC_URL;
    }

    public String getDocUrl() {
        return getContextPathWithoutTrailingSlash() + FrontendController.DOC_ULR;
    }

    /**
     * Get the called URL where the prefix is removed.
     * @param httpServletRequest The request
     * @param prefix The prefix. Make sure you add the starting and ending slash.
     * @return The relative called URL
     */
    public String getRelativeUrl(HttpServletRequest httpServletRequest, String prefix) {
        String requestedUri = (String) httpServletRequest.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return requestedUri.substring(prefix.length());
    }

    /**
     * Create {@link Artifact} based on the called URL. The URL must be of the format "context/prefix/groupId/artifactId/version/..." to work.
     * @param httpServletRequest The url.
     * @param prefix The prefix. Make sure you add the starting and ending slash.
     * @return {@link Artifact} object.
     */
    public Artifact createArtifactFromUrl(HttpServletRequest httpServletRequest, String prefix) {
        String relativeUrl = getRelativeUrl(httpServletRequest, prefix);
        String[] urlSegments = relativeUrl.split("/");
        String groupId = urlSegments[0];
        String artifactId = urlSegments[1];
        String version = urlSegments[2];

        return new Artifact(groupId, artifactId, version);
    }

    /**
     * Get the file path based on the called URL. The URL must be of the format "context/prefix/groupId/artifactId/version/**", where
     * the ** part is returned.
     * @param httpServletRequest The request.
     * @param prefix The prefix. Make sure you add the starting and ending slash.
     * @return The file path.
     */
    public String getFilePathFromUrl(HttpServletRequest httpServletRequest, String prefix) {
        String relativeUrl = getRelativeUrl(httpServletRequest, prefix);
        String[] urlSegments = relativeUrl.split("/");

        // Remove the first three segments, which represent the groupId, artifactId and version.
        urlSegments = Arrays.copyOfRange(urlSegments, 3, urlSegments.length);

        return String.join("/", urlSegments);
    }
}
