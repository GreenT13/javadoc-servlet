package com.apon.javadocservlet.controllers;

import com.apon.javadocservlet.controllers.apidoc.ApiDocController;
import com.apon.javadocservlet.controllers.frontend.FrontendController;
import com.apon.javadocservlet.repository.Artifact;
import org.springframework.ui.Model;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.ServletContext;
import java.util.Arrays;

/**
 * Class with methods to easily create URLs or get information from URLs.
 * To use Java code inside Thymeleaf templates, you need to configure the class as a Spring Bean. The only code that is
 * needed from code base is determining the correct URLs. That is why this class is the only Spring Bean used in templates.
 */
@SuppressWarnings("unused")
public class UrlUtil {
    private final String contextPath;

    public UrlUtil(ServletContext servletContext) {
        contextPath = getContextPathWithoutTrailingSlash(servletContext);
    }

    /**
     * @return The context path where the trailing slash is removed.
     */
    private String getContextPathWithoutTrailingSlash(ServletContext servletContext) {
        String contextPath = servletContext.getContextPath();

        // Remove the final slash if it exists, since this is already contained in the API_DOC_URL.
        if (contextPath.endsWith("/")) {
            contextPath = contextPath.substring(0, contextPath.length() - 1);
        }

        return contextPath;
    }

    /**
     * @return The relative URL that can be used to link to the home page.
     */
    public String getUrlToHome() {
        return contextPath + "/";
    }

    /**
     * @param artifact The artifact.
     * @param version  The version.
     * @return The URL to the index.html of the Javadoc for the given artifact and version.
     */
    public String createUrlToArtifact(Artifact artifact, String version) {
        return contextPath + FrontendController.DOC_ULR + artifact.getGroupId() + "/" + artifact.getArtifactId() + "/" + version + "/index.html";
    }

    /**
     * @param artifact The artifact.
     * @return The URL to the index.html of the Javadoc for the given artifact.
     */
    public String createUrlToArtifact(Artifact artifact) {
        return createUrlToArtifact(artifact, artifact.getVersion());
    }

    /**
     * @param artifact The artifact.
     * @return The URL to the search by groupId screen, where the groupId of the artifact is used.
     */
    public String createUrlToSearchByGroupId(Artifact artifact) {
        return contextPath + FrontendController.DOC_ULR + artifact.getGroupId();
    }

    /**
     * @param artifact The artifact.
     * @return The URL to the backend URL of the index.html of the javadoc of the artifact.
     */
    public String createApiDocUrlToArtifact(Artifact artifact) {
        return contextPath + ApiDocController.API_DOC_URL + artifact.getGroupId() + "/" + artifact.getArtifactId() + "/" + artifact.getVersion() + "/index.html";
    }

    /**
     * @return The base URL of {@link ApiDocController#getFileInZip(WebRequest)}.
     */
    public String getApiDocUrl() {
        return contextPath + ApiDocController.API_DOC_URL;
    }

    /**
     * @return The base URL of {@link FrontendController#iframe(Model, WebRequest)}.
     */
    public String getDocUrl() {
        return contextPath + FrontendController.DOC_ULR;
    }

    /**
     * @return The URL of {@link FrontendController#missingJavaDoc()}
     */
    public String getNoJavaDocUrl() {
        return contextPath + FrontendController.MISSING_JAVADOC_URL;
    }

    /**
     * Get the called URL where the prefix is removed.
     * @param webRequest The request.
     * @param prefix     The prefix. Make sure to add the starting and ending slash.
     * @return The relative called URL.
     */
    public String getRelativeUrl(WebRequest webRequest, String prefix) {
        String requestedUri = (String) ((ServletWebRequest) webRequest).getRequest().getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        return requestedUri.substring(prefix.length());
    }

    /**
     * Create {@link Artifact} based on the called URL. The URL must be of the format "context/prefix/groupId/artifactId/version/..." to work.
     * @param webRequest The request.
     * @param prefix     The prefix. Make sure you add the starting and ending slash.
     * @return {@link Artifact} object.
     */
    public Artifact createArtifactFromUrl(WebRequest webRequest, String prefix) {
        String relativeUrl = getRelativeUrl(webRequest, prefix);
        String[] urlSegments = relativeUrl.split("/");
        String groupId = urlSegments[0];
        String artifactId = urlSegments[1];
        String version = urlSegments[2];

        return new Artifact(groupId, artifactId, version);
    }

    /**
     * Get the file path based on the called URL. The URL must be of the format "context/prefix/groupId/artifactId/version/**", where
     * the ** part is returned.
     * @param webRequest The request.
     * @param prefix     The prefix. Make sure you add the starting and ending slash.
     * @return The file path.
     */
    public String getFilePathFromUrl(WebRequest webRequest, String prefix) {
        String relativeUrl = getRelativeUrl(webRequest, prefix);
        String[] urlSegments = relativeUrl.split("/");

        // Remove the first three segments, which represent the groupId, artifactId and version.
        urlSegments = Arrays.copyOfRange(urlSegments, 3, urlSegments.length);

        return String.join("/", urlSegments);
    }
}
