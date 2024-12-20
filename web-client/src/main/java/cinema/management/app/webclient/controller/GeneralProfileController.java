package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.dto.auth.response.AccessTokenResponseDto;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.UserNotFoundException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class GeneralProfileController {

    private final AuthRestClient authRestClient;

    @GetMapping
    public String getProfilePage() {
        try {
            AccessTokenResponseDto responseDto = authRestClient.getAccessToken(
                    SecurityUtil.getRefreshToken()
            );
            SecurityUtil.setAccessToken(responseDto.accessToken());
            return "profile";
        } catch (UserUnauthorizedException | BadRequestException | UserNotFoundException exception) {
            return "redirect:/login";
        }
    }

}
