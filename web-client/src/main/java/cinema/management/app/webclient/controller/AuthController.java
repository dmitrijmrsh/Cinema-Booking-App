package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.dto.auth.response.UserLogInResponseDto;
import cinema.management.app.webclient.exception.UserAlreadyExistException;
import cinema.management.app.webclient.exception.UserNotFoundException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthRestClient authRestClient;

    @GetMapping("/login")
    public String showLogInPage() {
        return "login";
    }

    @PostMapping("/login")
    public String logIn(
            @RequestParam final String email,
            @RequestParam final String password,
            Model model
    ) {
        try {
            UserLogInResponseDto dto = authRestClient.logIn(email, password);
            SecurityUtil.setAccessToken(dto.accessToken());
            SecurityUtil.setRefreshToken(dto.refreshToken());
            return "redirect:/";
        } catch (UserNotFoundException exception) {
            model.addAttribute("error", exception.getErrors().getFirst());
            return "login";
        }
    }

    @GetMapping("/signup")
    public String showSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(
            @RequestParam final String firstName,
            @RequestParam final String lastName,
            @RequestParam final String email,
            @RequestParam final String password,
            Model model
    ) {
        try {
            authRestClient.signUp(
                    firstName,
                    lastName,
                    email,
                    password
            );
            return "redirect:/";
        } catch (UserAlreadyExistException exception) {
            model.addAttribute("error", exception.getErrors().getFirst());
            return "redirect:/";
        }
    }

}
