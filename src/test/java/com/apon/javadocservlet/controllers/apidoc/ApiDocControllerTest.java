package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.controllers.ControllerTestUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.zip.ZipCache;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.ExecutionException;

import static com.apon.javadocservlet.controllers.apidoc.CacheControlMatcher.hasCacheControlWithEtag;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class ApiDocControllerTest {

    @Test
    public void urlIsParsedCorrectly() {
        // Given
        byte[] file = new byte[]{1, 2, 3};
        String checksum = "d0c488922fd11c46a96e4ca81063bfa3";
        ZipCache zipCache = ControllerTestUtil.createZipCache(file, checksum);
        ApiDocController apiDocController = new ApiDocController(zipCache, ControllerTestUtil.createUrlUtil());
        String groupId = "group.id";
        String artifactId = "artifact.id";
        String version = "1.2.3";
        String filePath = "subdir/index.html";
        String url = ApiDocController.API_DOC_URL + groupId + "/" + artifactId + "/" + version + "/" + filePath;
        WebRequest webRequest = ControllerTestUtil.createWebRequest(url);

        // When
        ResponseEntity<byte[]> response = apiDocController.getFileInZip(webRequest);

        // Then
        // Verify the response entity.
        assertThat(response.getBody(), equalTo(file));
        assertThat(response, hasCacheControlWithEtag(checksum));

        // Verify that all the arguments are correctly parsed and passed to the zipCache object.
        ArgumentCaptor<Artifact> artifactArgument = ArgumentCaptor.forClass(Artifact.class);
        ArgumentCaptor<String> filePathArgument = ArgumentCaptor.forClass(String.class);
        verify(zipCache).getContentOfFileFromZip(artifactArgument.capture(), filePathArgument.capture());
        assertThat(artifactArgument.getValue().getGroupId(), equalTo(groupId));
        assertThat(artifactArgument.getValue().getArtifactId(), equalTo(artifactId));
        assertThat(artifactArgument.getValue().getVersion(), equalTo(version));
        assertThat(filePathArgument.getValue(), equalTo(filePath));
    }

    @Test
    public void return200WhenFileCouldNotBeFound() {
        // Given
        String checksum = "d0c488922fd11c46a96e4ca81063bfa3";
        ZipCache zipCache = ControllerTestUtil.createZipCache(null, checksum);
        ApiDocController apiDocController = new ApiDocController(zipCache, ControllerTestUtil.createUrlUtil());
        WebRequest webRequest = ControllerTestUtil.createWebRequest(ApiDocController.API_DOC_URL + "url/does/not/matter");

        // When
        ResponseEntity<byte[]> response = apiDocController.getFileInZip(webRequest);

        // Then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response, hasCacheControlWithEtag(checksum));
    }

    @Test
    public void return304WithCacheControlWhenEtagMatches() {
        // Given
        String checksum = "d0c488922fd11c46a96e4ca81063bfa3";
        ZipCache zipCache = ControllerTestUtil.createZipCache(null, checksum);
        ApiDocController apiDocController = new ApiDocController(zipCache, ControllerTestUtil.createUrlUtil());
        WebRequest webRequest = ControllerTestUtil.createWebRequest(ApiDocController.API_DOC_URL + "url/does/not/matter");
        doReturn(checksum).when(webRequest).getHeader(HttpHeaders.IF_NONE_MATCH);

        // When
        ResponseEntity<byte[]> response = apiDocController.getFileInZip(webRequest);

        // Then
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_MODIFIED));
        assertThat(response, hasCacheControlWithEtag(checksum));
    }
}
