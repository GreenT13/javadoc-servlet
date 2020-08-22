package com.apon.javadocservlet.controllers.frontend;

import com.apon.javadocservlet.controllers.ApplicationException;
import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.ArtifactVersions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.context.request.WebRequest;

import java.util.List;

/**
 * Controller for showing Thymeleaf pages.
 */
@Controller
public class FrontendController {
    public final static String DOC_ULR = "/doc/";
    public final static String MISSING_JAVADOC_URL = "/missing_javadoc";

    private final ArtifactStorage artifactStorage;
    private final UrlUtil urlUtil;

    public FrontendController(ArtifactStorage artifactStorage, UrlUtil urlUtil) {
        this.artifactStorage = artifactStorage;
        this.urlUtil = urlUtil;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("formObject", new FrontendForm());
        return "home";
    }

    @PostMapping("/")
    public String search(Model model, @ModelAttribute FrontendForm frontendForm) {
        try {
            // Add the same form again, so details stay on the screen.
            model.addAttribute("formObject", frontendForm);

            // Search for an artifact and show them.
            List<Artifact> artifacts = artifactStorage.findArtifacts(frontendForm.getGroupId(), frontendForm.getArtifactId());
            model.addAttribute("foundArtifacts", artifacts);

            return "home";
        } catch (ArtifactSearchException e) {
            throw new ApplicationException(e);
        }
    }

    @GetMapping(DOC_ULR + "**")
    public String iframe(Model model, WebRequest webRequest) {
        try {
            Artifact artifact = urlUtil.createArtifactFromUrl(webRequest, DOC_ULR);
            ArtifactVersions artifactVersions = artifactStorage.findArtifactVersions(artifact);
            List<Artifact> artifacts = artifactStorage.findArtifacts(artifact.getGroupId(), null);

            // Add attribute indicating if we have a javadoc jar or not.
            // This information can be found in the just retrieved artifactVersions.
            ArtifactVersions.Version currentVersion = artifactVersions.getVersions().stream()
                    .filter(version -> version.getVersion().equals(artifact.getVersion()))
                    .findAny()
                    .orElseThrow(() -> new ApplicationException("No artifact versions found for " + artifact.getGroupId() + ":" + artifact.getArtifactId()));
            model.addAttribute("hasNoJavaDocJar", !currentVersion.isHasJavaDocJar());

            model.addAttribute("apiDocUrl", urlUtil.createApiDocUrlToArtifact(artifact));
            model.addAttribute("selectedArtifact", artifact);
            model.addAttribute("artifacts", artifacts);
            model.addAttribute("artifactVersions", artifactVersions);

            // The link showing the groupId on screen should be able to redirect to the home screen, with the groupId
            // filled as search option. The frontendForm object needs to be created in Java.
            FrontendForm frontendForm = new FrontendForm();
            frontendForm.setGroupId(artifact.getGroupId());
            model.addAttribute("formObject", frontendForm);
            return "iframe";
        } catch (ArtifactSearchException e) {
            throw new ApplicationException(e);
        }
    }

    @GetMapping(MISSING_JAVADOC_URL)
    public String missingJavaDoc() {
        return "missing_javadoc";
    }
}
