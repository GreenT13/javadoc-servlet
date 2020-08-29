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
    public static final String DOC_ULR = "/doc/";
    public static final String MISSING_JAVADOC_URL = "/missing_javadoc";

    private final ArtifactStorage artifactStorage;
    private final UrlUtil urlUtil;

    // Names of model attributes that we are allowed to use. Don't change these: they are also used in Thymeleaf templates.
    static final String MODEL_FORM_OBJECT = "formObject";
    static final String MODEL_FOUND_ARTIFACTS = "foundArtifacts";
    static final String MODEL_HAS_JAVADOC_JAR = "hasJavaDocJar";
    static final String MODEL_API_DOC_URL = "apiDocUrl";
    static final String MODEL_SELECTED_ARTIFACT = "selectedArtifact";
    static final String MODEL_ARTIFACTS = "artifacts";
    static final String MODEL_ARTIFACT_VERSIONS = "artifactVersions";

    public FrontendController(ArtifactStorage artifactStorage, UrlUtil urlUtil) {
        this.artifactStorage = artifactStorage;
        this.urlUtil = urlUtil;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute(MODEL_FORM_OBJECT, new FrontendForm());
        return "home";
    }

    @PostMapping("/")
    public String search(Model model, @ModelAttribute FrontendForm frontendForm) {
        try {
            // Add the same form again, so details stay on the screen.
            model.addAttribute(MODEL_FORM_OBJECT, frontendForm);

            // Search for an artifact and show them.
            List<Artifact> artifacts = artifactStorage.findArtifacts(frontendForm.getGroupId(), frontendForm.getArtifactId());
            model.addAttribute(MODEL_FOUND_ARTIFACTS, artifacts);

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
            model.addAttribute(MODEL_HAS_JAVADOC_JAR, currentVersion.isHasJavaDocJar());

            model.addAttribute(MODEL_API_DOC_URL, urlUtil.createApiDocUrlToArtifact(artifact));
            model.addAttribute(MODEL_SELECTED_ARTIFACT, artifact);
            model.addAttribute(MODEL_ARTIFACTS, artifacts);
            model.addAttribute(MODEL_ARTIFACT_VERSIONS, artifactVersions);

            // The link showing the groupId on screen should be able to redirect to the home screen, with the groupId
            // filled as search option. The frontendForm object needs to be created in Java.
            FrontendForm frontendForm = new FrontendForm();
            frontendForm.setGroupId(artifact.getGroupId());
            model.addAttribute(MODEL_FORM_OBJECT, frontendForm);
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
