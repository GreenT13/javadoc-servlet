package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Controller
public class ApiDocController {
    public final static String API_DOC_URL = "/apidoc/";

    private final ZipCache zipCache;
    private final UrlUtil urlUtil;

    public ApiDocController(ZipCache zipCache, UrlUtil urlUtil) {
        this.zipCache = zipCache;
        this.urlUtil = urlUtil;
    }

    @GetMapping(API_DOC_URL + "**")
    @ResponseBody
    public byte[] getFileInZip(HttpServletRequest request) throws ArtifactSearchException, ExecutionException {
        Artifact artifact = urlUtil.createArtifactFromUrl(request, API_DOC_URL);
        String filePath = urlUtil.getFilePathFromUrl(request, API_DOC_URL);

        Optional<byte[]> optionalFileContent = zipCache.getContentOfFileFromZip(artifact.getGroupId(), artifact.getArtifactId(), artifact.getVersion(), filePath);
        if (optionalFileContent.isEmpty()) {
            throw new ArtifactSearchException("Could not find file.");
        }

        return optionalFileContent.get();
    }
}
