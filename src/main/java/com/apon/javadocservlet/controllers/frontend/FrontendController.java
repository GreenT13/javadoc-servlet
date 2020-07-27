package com.apon.javadocservlet.controllers.frontend;

import com.apon.javadocservlet.controllers.ControllerUtil;
import com.apon.javadocservlet.repository.Artifact;
import com.apon.javadocservlet.repository.ArtifactSearchException;
import com.apon.javadocservlet.repository.ArtifactStorage;
import com.apon.javadocservlet.repository.impl.local.SingleArtifactTest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class FrontendController {

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
        ArtifactStorage artifactStorage = new SingleArtifactTest();
        List<Artifact> artifacts = artifactStorage.findArtifacts(frontendForm.getGroupId(), frontendForm.getArtifactId());
        model.addAttribute("foundArtifacts", artifacts);

        return "home";
    }

    @GetMapping("/doc/**")
    public String iframe(Model model, HttpServletRequest request) {
        String requestUrl = ControllerUtil.getRelativeUrl(request, "/doc/");

        model.addAttribute("url", "/apidoc/" + requestUrl);
        return "iframe";
    }
}
