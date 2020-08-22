package com.apon.javadocservlet.controllers;

import com.apon.javadocservlet.controllers.apidoc.ApiDocController;
import com.apon.javadocservlet.controllers.frontend.FrontendController;
import com.apon.javadocservlet.repository.Artifact;
import org.junit.jupiter.api.Test;

import static com.apon.javadocservlet.controllers.ControllerTestUtil.createUrlUtil;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UrlUtilTest {
    /** String that can be used as context path. Unless a specific value is used, use this one. */
    private final static String CONTEXT_PATH = "/context-path";

    @Test
    public void homeUrlIsContextPathFollowedBySlash() {
        // Given
        UrlUtil urlUtil = createUrlUtil(CONTEXT_PATH);

        // When and then
        assertThat(urlUtil.getUrlToHome(), equalTo(CONTEXT_PATH + "/"));
    }

    @Test
    public void correctUrlsAreGenerated() {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "1.0");
        UrlUtil urlUtil = createUrlUtil(CONTEXT_PATH);

        // When and then
        assertThat(urlUtil.createUrlToArtifact(artifact), equalTo(CONTEXT_PATH + FrontendController.DOC_ULR + "groupId/artifactId/1.0/index.html"));
        assertThat(urlUtil.createUrlToArtifact(artifact, "2.0"), equalTo(CONTEXT_PATH + FrontendController.DOC_ULR + "groupId/artifactId/2.0/index.html"));
        assertThat(urlUtil.createApiDocUrlToArtifact(artifact), equalTo(CONTEXT_PATH + ApiDocController.API_DOC_URL + "groupId/artifactId/1.0/index.html"));
    }

    @Test
    public void correctBaseUrlsAreGenerated() {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "1.0");
        UrlUtil urlUtil = createUrlUtil(CONTEXT_PATH);

        // When
        assertThat(urlUtil.createApiDocUrlToArtifact(artifact), startsWith(urlUtil.getApiDocUrl()));
        assertThat(urlUtil.createUrlToArtifact(artifact), startsWith(urlUtil.getDocUrl()));
    }

    @Test
    public void contextPathEndingWithSlashIsHandledCorrectly() {
        // Given
        UrlUtil urlUtil = createUrlUtil(CONTEXT_PATH + "/");

        // When
        assertThat(urlUtil.getUrlToHome(), equalTo(CONTEXT_PATH + "/"));
    }

    @Test
    public void exceptionIsThrownIfUrlIsIncorrect() {
        // Given
        UrlUtil urlUtil = createUrlUtil(CONTEXT_PATH + "/");
        String incorrectUrl = "my.group.id/my.artifact.id";

        // When
        assertThrows(ApplicationException.class, () -> urlUtil.splitUrl(incorrectUrl));
    }
}


