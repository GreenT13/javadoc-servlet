package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.controllers.ControllerTestUtil;
import com.apon.javadocservlet.zip.ZipCache;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;

class ApiDocControllerTest {

    @Test
    public void urlIsParsedCorrectly() throws ExecutionException {
        // Given
        ZipCache zipCache = mock(ZipCache.class);
        byte[] file = new byte[]{1, 2, 3};
        doReturn(Optional.of(file)).when(zipCache).getContentOfFileFromZip(anyString(), anyString(), anyString(), anyString());
        doReturn("md5").when(zipCache).getMd5HashFromZip(anyString(), anyString(), anyString());

        WebRequest webRequest = mock(WebRequest.class);
        doReturn(false).when(webRequest).checkNotModified(anyString());

        ApiDocController apiDocController = new ApiDocController(zipCache, ControllerTestUtil.createUrlUtil());
        String groupId = "group.id";
        String artifactId = "artifact.id";
        String version = "1.2.3";
        String filePath = "subdir/index.html";
        String url = ApiDocController.API_DOC_URL + groupId + "/" + artifactId + "/" + version + "/" + filePath;
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        doReturn(url).when(httpServletRequest).getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        // When
        byte[] response = apiDocController.getFileInZip(httpServletRequest, webRequest).getBody();

        // Then
        // Verify the same file content is returned.
        assertThat(response, equalTo(file));

        // Verify that all the arguments are correctly parsed and passed to the zipCache object.
        ArgumentCaptor<String> groupIdArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> artifactIdArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> versionArgument = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> filePathArgument = ArgumentCaptor.forClass(String.class);
        verify(zipCache).getContentOfFileFromZip(groupIdArgument.capture(), artifactIdArgument.capture(), versionArgument.capture(), filePathArgument.capture());
        assertThat(groupIdArgument.getValue(), equalTo(groupId));
        assertThat(artifactIdArgument.getValue(), equalTo(artifactId));
        assertThat(versionArgument.getValue(), equalTo(version));
        assertThat(filePathArgument.getValue(), equalTo(filePath));
    }

    @Test
    public void notFoundIsReturnedWhenFileCouldNotBeFound() throws ExecutionException {
        // Given
        ZipCache zipCache = mock(ZipCache.class);
        doReturn(Optional.empty()).when(zipCache).getContentOfFileFromZip(anyString(), anyString(), anyString(), anyString());
        doReturn("md5").when(zipCache).getMd5HashFromZip(anyString(), anyString(), anyString());

        WebRequest webRequest = mock(WebRequest.class);
        doReturn(false).when(webRequest).checkNotModified(anyString());

        ApiDocController apiDocController = new ApiDocController(zipCache, ControllerTestUtil.createUrlUtil());
        HttpServletRequest httpServletRequest = mock(HttpServletRequest.class);
        doReturn(ApiDocController.API_DOC_URL + "url/does/not/matter").when(httpServletRequest).getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

        // When
        HttpStatus status = apiDocController.getFileInZip(httpServletRequest, webRequest).getStatusCode();

        // Then
        assertThat(status, equalTo(HttpStatus.NOT_FOUND));
    }
}
