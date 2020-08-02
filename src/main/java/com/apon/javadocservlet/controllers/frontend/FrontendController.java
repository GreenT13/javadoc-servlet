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

    @GetMapping(DOC_ULR + "*")
    public String searchByGroupId(Model model, HttpServletRequest request) throws ArtifactSearchException {
        String groupId = urlUtil.getRelativeUrl(request, DOC_ULR);

        List<Artifact> artifacts = artifactStorage.findArtifacts(groupId, null);

        model.addAttribute("groupId", groupId);
        model.addAttribute("foundArtifacts", artifacts);
        return "search_by_group_id";
    }

    public final static String MISSING_JAVADOC_URL = "/missing_javadoc";

    @GetMapping(MISSING_JAVADOC_URL)
    public String missingJavaDoc() {
        return "missing_javadoc";
    }
}
