package com.apon.javadocservlet.controllers.frontend;

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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FrontendController {
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
    public String search(Model model, @ModelAttribute FrontendForm frontendForm) throws ArtifactSearchException {
        // Add the same form again, so details stay on the screen.
        model.addAttribute("formObject", frontendForm);

        // Search for an artifact and show them.
        List<Artifact> artifacts = artifactStorage.findArtifacts(frontendForm.getGroupId(), frontendForm.getArtifactId());
        model.addAttribute("foundArtifacts", artifacts);

        return "home";
    }

    public final static String DOC_ULR = "/doc/";

    @GetMapping(DOC_ULR + "**")
    public String iframe(Model model, HttpServletRequest request) throws ArtifactSearchException {
        Artifact artifact = urlUtil.createArtifactFromUrl(request, DOC_ULR);
        ArtifactVersions artifactVersions = artifactStorage.findArtifactVersions(artifact);

        model.addAttribute("apiDocUrl", urlUtil.createApiDocUrlToArtifact(artifact));
        model.addAttribute("artifact", artifact);
        model.addAttribute("artifactVersions", artifactVersions);
        return "iframe";
    }
}
