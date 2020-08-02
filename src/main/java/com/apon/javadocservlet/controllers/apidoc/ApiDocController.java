package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.controllers.ControllerUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Controller
public class ApiDocController {
    private final ZipCache zipCache;

    public ApiDocController(ZipCache zipCache) {
        this.zipCache = zipCache;
    }

    @GetMapping("/apidoc/**")
    @ResponseBody
    public byte[] getFileInZip(HttpServletRequest request) throws ArtifactSearchException, ExecutionException {
        String requestUrl = ControllerUtil.getRelativeUrl(request, "/apidoc/");

        Artifact artifact = createArtifactFromUrl(requestUrl);
        String filePath = getFilePathFromUrl(requestUrl);

        Optional<byte[]> optionalFileContent = zipCache.getContentOfFileFromZip(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), filePath);
        if (optionalFileContent.isEmpty()) {
            throw new ArtifactSearchException("Could not find file.");
        }

        return optionalFileContent.get();
    }

    /**
     * Create {@link Artifact} based on the given url. The URL must be of the format "groupId/artifactId/version/..." to work.
     * @param url The url.
     * @return {@link Artifact} object.
     */
    public static Artifact createArtifactFromUrl(String url) {
        String[] urlSegments = url.split("/");
        String groupId = urlSegments[0];
        String artifactId = urlSegments[1];
        String version = urlSegments[2];

        return new Artifact(groupId, artifactId, version);
    }

    /**
     * Get the file path based on the given url. The URL must be of the format "groupId/artifactId/version/**", where
     * the ** part is returned.
     * @param url The url.
     * @return The file path.
     */
    private String getFilePathFromUrl(String url) {
        String[] urlSegments = url.split("/");

        // Remove the first three segments, which represent the groupId, artifactId and version.
        urlSegments = Arrays.copyOfRange(urlSegments, 3, urlSegments.length);

        return String.join("/", urlSegments);
    }
}
