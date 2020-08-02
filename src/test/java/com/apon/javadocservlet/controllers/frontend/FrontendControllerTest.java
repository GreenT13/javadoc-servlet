package com.apon.javadocservlet.controllers.frontend;

import com.apon.javadocservlet.controllers.ControllerTestUtil;
import com.apon.javadocservlet.controllers.apidoc.ApiDocController;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class FrontendControllerTest {

    @Test
    public void formObjectIsSetForHomePage() {
        // Given
        FrontendController frontendController = new FrontendController(null, ControllerTestUtil.createUrlUtil());
        Model model = mock(Model.class);

        // When
        String response = frontendController.homePage(model);

        // Then
        assertThat("Home page must be shown", response, equalTo("home"));

        ArgumentCaptor<FrontendForm> frontendFormArgument = ArgumentCaptor.forClass(FrontendForm.class);
        verify(model).addAttribute(eq("formObject"), frontendFormArgument.capture());
        assertThat(frontendFormArgument.getValue(), notNullValue());
    }

    @Test
    public void artifactsAreSearched() throws ArtifactSearchException {
        // Given
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        List<Artifact> artifactList = Collections.singletonList(new Artifact("groupId", "artifactId", "version"));
        doReturn(artifactList).when(artifactStorage).findArtifacts(anyString(), anyString());
        FrontendController frontendController = new FrontendController(artifactStorage, ControllerTestUtil.createUrlUtil());
        FrontendForm frontendForm = new FrontendForm();
        String groupId = "groupId";
        String artifactId = "artifactId";
        frontendForm.setGroupId(groupId);
        frontendForm.setArtifactId(artifactId);
        Model model = mock(Model.class);

        // When
        String response = frontendController.search(model, frontendForm);

        // Then
        assertThat("Home page must be shown", response, equalTo("home"));

        ArgumentCaptor<?> argumentCaptor = ArgumentCaptor.forClass(artifactList.getClass());
        verify(model).addAttribute(eq("foundArtifacts"), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), equalTo(artifactList));
    }

    @Test
    public void iframeUrlHasDocReplacedWithApiDoc() throws ArtifactSearchException {
        // Given
        ArtifactStorage artifactStorage = mock(ArtifactStorage.class);
        List<Artifact> artifactList = Collections.singletonList(new Artifact("groupId", "artifactId", "version"));
        doReturn(artifactList).when(artifactStorage).findArtifacts(anyString(), anyString());
        FrontendController frontendController = new FrontendController(artifactStorage, ControllerTestUtil.createUrlUtil());
        Model model = mock(Model.class);
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        // We must end with index.html, because that is the default page to open.
        String afterDocUrl = "groupId/artifactId/version/index.html";
        doReturn(FrontendController.DOC_ULR + afterDocUrl).when(httpServletRequest).getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        // When
        String response = frontendController.iframe(model, httpServletRequest);

        // Then
        assertThat("Iframe page must be shown", response, equalTo("iframe"));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(model).addAttribute(eq("apiDocUrl"), argumentCaptor.capture());
        assertThat(argumentCaptor.getValue(), equalTo(ApiDocController.API_DOC_URL + afterDocUrl));
    }

}
