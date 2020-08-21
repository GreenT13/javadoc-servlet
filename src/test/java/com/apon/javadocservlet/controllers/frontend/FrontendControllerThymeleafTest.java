package com.apon.javadocservlet.controllers.frontend;

import com.apon.javadocservlet.controllers.apidoc.ApiDocController;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.ArtifactVersions;
import com.apon.javadocservlet.zip.TestZipConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class FrontendControllerThymeleafTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtifactStorage artifactStorage;

    @Test
    public void testHomepage() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");

        // When
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @Test
    public void searchOnHomepageShowsArtifactInformation() throws Exception {
        // Given
        String groupId = "my.group.id";
        String artifactId = "my.artifact.id";
        String version = "1.0-SNAPSHOT";
        Artifact artifact = new Artifact(groupId, artifactId, version);
        when(artifactStorage.findArtifacts(artifact.getGroupId(), artifactId)).thenReturn(Collections.singletonList(artifact));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/")
                .param("groupId", groupId)
                .param("artifactId", artifactId)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        // When
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                // Artifact details are shown on screen.
                .andExpect(content().string(allOf(
                        containsString(groupId),
                        containsString(artifactId),
                        containsString(version)
                )));
    }

    @Test
    public void testMissingJavadoc() throws Exception {
        // Given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(FrontendController.MISSING_JAVADOC_URL);

        // When
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("missing_javadoc"))
                .andExpect(content().string(containsString("No javadoc could be found for this jar!")));
    }

    @Test
    public void docAutomaticallyRedirectsToIndexHtml() throws Exception {
        // Given
        String groupId = "my.group.id";
        String artifactId = "my.artifact.id";
        String version = "1.0-SNAPSHOT";
        Artifact artifact = new Artifact(groupId, artifactId, version);
        when(artifactStorage.findArtifacts(artifact.getGroupId(), artifactId)).thenReturn(Collections.singletonList(artifact));
        when(artifactStorage.getJavaDocJar(artifact)).thenReturn(TestZipConstants.FILE);
        when(artifactStorage.findArtifactVersions(artifact)).thenReturn(new ArtifactVersions(Collections.singletonList(new ArtifactVersions.Version(version, true))));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(FrontendController.DOC_ULR + groupId + "/" + artifactId + "/" + version);

        // When
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(view().name("iframe"))
                // IFrame is shown with URL to the index.html.
                .andExpect(content().string(
                        containsString(ApiDocController.API_DOC_URL + groupId + "/" + artifactId + "/" + version + "/index.html")
                ));
    }
}
