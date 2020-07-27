package com.apon.javadocservlet.controllers.apidoc;

import com.apon.javadocservlet.controllers.ControllerUtil;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.impl.local.SingleArtifactTest;
import com.apon.javadocservlet.zip.ZipCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Controller
public class ApiDocController {
    private final static String GROUP_ID = "one.util";
    private final static String ARTIFACT_ID = "streamex";
    private final static String VERSION = "0.7.2";

    private final ArtifactStorage artifactStorage = new SingleArtifactTest();
    private final ZipCache zipCache = new ZipCache(artifactStorage);

    @GetMapping("/apidoc/**")
    @ResponseBody
    public byte[] getFileInZip(HttpServletRequest request) throws ArtifactSearchException, IOException, ExecutionException {
        String requestUrl = ControllerUtil.getRelativeUrl(request, "/apidoc/");

        Optional<byte[]> optionalFileContent = zipCache.getContentOfFileFromZip(GROUP_ID, ARTIFACT_ID, VERSION, requestUrl);
        if (optionalFileContent.isEmpty()) {
            throw new ArtifactSearchException("Could not find file.");
        }

        return optionalFileContent.get();
    }
}
