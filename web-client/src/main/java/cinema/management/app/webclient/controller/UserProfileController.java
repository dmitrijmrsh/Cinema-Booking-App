package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.exception.UserNotFoundException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile/me")
@RequiredArgsConstructor
public class UserProfileController {

    private final AuthRestClient authRestClient;

    @GetMapping
    public String getUserProfilePage(Model model) {
        try {
            model.addAttribute(
                    "user",
                    authRestClient.getCurrentUserInfo()
            );
            return "user-profile";
        } catch (UserUnauthorizedException | UserNotFoundException exception) {
            return "redirect:/login";
        }
    }

    @GetMapping("/update")
    public String getUpdateUserProfilePage() {
        return "update-user-profile";
    }

    @PostMapping("/update")
    public String updateUserProfilePage(
            @RequestParam final String firstName,
            @RequestParam final String lastName,
            @RequestParam final String email
    ) {
        try {
            authRestClient.updateCurrentUser(
                    firstName,
                    lastName,
                    email
            );
            return "redirect:/login";
        } catch (UserUnauthorizedException | UserNotFoundException exception) {
            return "redirect:/login";
        }
    }

    @PostMapping("/logout")
    public String logOut() {
        authRestClient.logOut();
        SecurityUtil.setAccessToken("");
        SecurityUtil.setRefreshToken("");
        return "redirect:/";
    }

}
