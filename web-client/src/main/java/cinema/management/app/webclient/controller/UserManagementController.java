package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.exception.AccessDeniedException;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile/special-features/users")
@RequiredArgsConstructor
public class UserManagementController {

    private final AuthRestClient authRestClient;

    @GetMapping
    public String getUserManagementPage(Model model) {
        try {
            model.addAttribute(
                    "users",
                    authRestClient.getAllUsers()
            );
            return "user-management";
        } catch (UserUnauthorizedException | AccessDeniedException exception) {
            return "redirect:/login";
        }
    }

    @GetMapping("/make-manager")
    public String getMakeManager() {
        return "make-manager";
    }


    @PostMapping("/make-manager")
    public String makeUserManager(
            @RequestParam final Integer userId,
            Model model
    ) {
        try {
            authRestClient.setUserRoleToManager(userId);
        } catch (UserUnauthorizedException | AccessDeniedException exception) {
            return "redirect:/login";
        } catch (BadRequestException exception) {
            model.addAttribute(
                    "error",
                    "Некорректный формат данных"
            );
            return "make-manager";
        }

        model.addAttribute(
                "users",
                authRestClient.getAllUsers()
        );
        return "user-management";
    }

}
