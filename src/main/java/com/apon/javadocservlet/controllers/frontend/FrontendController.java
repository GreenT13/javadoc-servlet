package com.apon.javadocservlet.controllers.frontend;

import com.apon.javadocservlet.controllers.UrlUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.ArtifactVersions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
    public String homePage(Model model, @RequestParam(required = false) String groupId) throws ArtifactSearchException {
        FrontendForm frontendForm = new FrontendForm();

        // If the groupId is filled, we want to search on the groupId.
        if (groupId != null) {
            frontendForm.setGroupId(groupId);
            return search(model, frontendForm);
        }

        // Just show the home page.
        model.addAttribute("formObject", new FrontendForm());
        return "home";
    }

    @PostMapping("/")
    public String search(Model model, @ModelAttribute FrontendForm frontendForm) throws ArtifactSearchException {
        // Add the same form again, so details stay on the screen.
        model.addAttribute("formObject", frontendForm);

        // Search for an artifact and show them.
        List<Artifact> artifacts = artifactStorage.findArtifacts(frontendForm.getGroupId(), frontendForm.getArtifactId());
        model.addAttribute("foundArtifacts", artifacts);

        return "home";
    }

    @GetMapping(DOC_ULR + "**")
    public String iframe(Model model, WebRequest webRequest) throws ArtifactSearchException {
        Artifact artifact = urlUtil.createArtifactFromUrl(webRequest, DOC_ULR);
        ArtifactVersions artifactVersions = artifactStorage.findArtifactVersions(artifact);
        List<Artifact> artifacts = artifactStorage.findArtifacts(artifact.getGroupId(), null);

        // Add attribute indicating if we have a javadoc jar or not.
        // This information can be found in the just retrieved artifactVersions.
        ArtifactVersions.Version currentVersion = artifactVersions.getVersions().stream()
                .filter(version -> version.getVersion().equals(artifact.getVersion()))
                .findAny()
                .orElseThrow(ArtifactSearchException::new);
        model.addAttribute("hasNoJavaDocJar", !currentVersion.isHasJavaDocJar());

        model.addAttribute("apiDocUrl", urlUtil.createApiDocUrlToArtifact(artifact));
        model.addAttribute("selectedArtifact", artifact);
        model.addAttribute("artifacts", artifacts);
        model.addAttribute("artifactVersions", artifactVersions);
        return "iframe";
    }

    @GetMapping(MISSING_JAVADOC_URL)
    public String missingJavaDoc() {
        return "missing_javadoc";
    }
}
