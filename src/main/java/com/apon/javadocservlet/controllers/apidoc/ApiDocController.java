package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.controllers.ControllerUtil;
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

        String[] urlSegments = requestUrl.split("/");
        String groupId = urlSegments[0];
        String artifactId = urlSegments[1];
        String version = urlSegments[2];
        // Path will be the rest of the array.
        String filePath = String.join("/", Arrays.copyOfRange(urlSegments, 3, urlSegments.length));

        Optional<byte[]> optionalFileContent = zipCache.getContentOfFileFromZip(groupId, artifactId, version, filePath);
        if (optionalFileContent.isEmpty()) {
            throw new ArtifactSearchException("Could not find file.");
        }

        return optionalFileContent.get();
    }
}
