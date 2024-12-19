package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.screening.ScreeningRestClient;
import cinema.management.app.webclient.exception.AccessDeniedException;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.InvalidScreeningTimeFrameException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
@RequestMapping("/profile/special-features/screenings")
@RequiredArgsConstructor
public class ScreeningsManagementController {

    private final ScreeningRestClient screeningRestClient;

    @GetMapping
    public String getScreeningManagementPage() {
        return "screening-management";
    }

    @GetMapping("/create")
    public String getCreateScreeningPage() {
        return "create-screening";
    }

    @PostMapping("/create")
    public String createScreening(
            @RequestParam final LocalDate date,
            @RequestParam final LocalTime time,
            @RequestParam final Integer filmId,
            @RequestParam final Integer hallId,
            Model model
    ) {
       try {
           screeningRestClient.createScreening(
                   date,
                   time,
                   filmId,
                   hallId
           );
           return "special-features";
       } catch (UserUnauthorizedException exception) {
           return "redirect:/login";
       } catch (BadRequestException | InvalidScreeningTimeFrameException exception) {
           model.addAttribute(
                   "error",
                   "Некорректные данные для сеанса"
           );
           return "create-screening";
       } catch (AccessDeniedException exception) {
           return "redirect:/";
       }
    }

    @PostMapping("/delete-expired")
    public String deletePassedScreenings() {
        try {
            screeningRestClient.deleteAllPassedScreenings();
            return "special-features";
        } catch (UserUnauthorizedException | AccessDeniedException exception) {
            return "redirect:/";
        }
    }
}
