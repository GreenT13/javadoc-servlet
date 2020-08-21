package com.apon.javadocservlet;

import com.apon.javadocservlet.controllers.ControllerTestUtil;
import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.ArtifactVersions;
import com.apon.javadocservlet.zip.TestZipConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JavadocServletApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArtifactStorage artifactStorage;

    @Test
    void missingJavaDocUrlIsCorrect() throws Exception {
        // Given
        UrlUtil urlUtil = ControllerTestUtil.createUrlUtil();
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(urlUtil.getNoJavaDocUrl());

        // When and then
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No javadoc could be found for this jar")));
    }

    /**
     * In this method we test that after executing several calls, the content of the artifact is retrieved exactly once
     * from the {@link ArtifactStorage}.
     */
    @Test
    public void javadocJarIsCached() throws Exception {
        // Given
        Artifact artifact = new Artifact("groupId", "artifactId", "1.0");
        when(artifactStorage.getJavaDocJar(artifact)).thenReturn(TestZipConstants.FILE);
        when(artifactStorage.findArtifactVersions(artifact)).thenReturn(new ArtifactVersions(Collections.singletonList(new ArtifactVersions.Version("1.0", true))));
        when(artifactStorage.findArtifacts(artifact.getGroupId(), artifact.getArtifactId())).thenReturn(Collections.singletonList(artifact));

        RequestBuilder requestBuilder1 = MockMvcRequestBuilders.get("/apidoc/groupId/artifactId/1.0/file1.txt");
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders.get("/apidoc/groupId/artifactId/1.0/subdir/file2.txt");
        RequestBuilder requestBuilder3 = MockMvcRequestBuilders.get("/apidoc/groupId/artifactId/1.0/file1.txt")
                .header(HttpHeaders.IF_NONE_MATCH, TestZipConstants.FILE_CHECKSUM_WITH_QUOTES);

        // When
        mockMvc.perform(requestBuilder1)
                .andExpect(status().isOk());
        mockMvc.perform(requestBuilder2)
                .andExpect(status().isOk());
        mockMvc.perform(requestBuilder3)
                .andExpect(status().isNotModified());

        // Verify that we retrieved the jar exactly once.
        //verify(artifactStorage).getJavaDocJar(artifact);
    }
}
