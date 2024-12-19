package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.exception.AccessDeniedException;
import cinema.management.app.webclient.exception.UserNotFoundException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile/special-features")
@RequiredArgsConstructor
public class SpecialFeaturesController {

    private final AuthRestClient authRestClient;

    @GetMapping
    public String getSpecialFeaturesPage(Model model) {
        try {
            authRestClient.checkAccessSpecialFeatures();
            return "special-features";
        } catch (UserUnauthorizedException | UserNotFoundException exception) {
            return "redirect:/";
        } catch (AccessDeniedException exception) {
            model.addAttribute("error", exception.getErrors().getFirst());
            return "profile";
        }
    }
}
