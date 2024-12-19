package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.screening.ScreeningRestClient;
import cinema.management.app.webclient.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/screenings")
@RequiredArgsConstructor
public class ScreeningsController {

    private final ScreeningRestClient screeningRestClient;

    @GetMapping
    public String getScreeningsList(Model model) {
        try {
            model.addAttribute(
                    "screenings",
                    screeningRestClient.findAllScreenings()
            );
            return "screenings";
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        }
    }

    @GetMapping("/{id:\\d+}")
    public String getScreeningById(
            @PathVariable("id") final Integer id,
            Model model
    ) {
        try {
            model.addAttribute(
                    "screening",
                    screeningRestClient.findScreeningById(id)
            );
            return "screening-details";
        } catch (ScreeningNotFoundException exception) {
            return "redirect:/screenings";
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        }
    }

    @PostMapping("/{screeningId:\\d+}/seats")
    public String reserveSeat(
            @PathVariable("screeningId") final Integer screeningId,
            Model model
    ) {
        try {
            screeningRestClient.reserveSeat(screeningId);
            model.addAttribute("success", "Место успешно забронировано");
        } catch (ScreeningNotFoundException exception) {
          model.addAttribute("error", "Киносеанс не найден");
        } catch (ScreeningPassedException exception) {
            model.addAttribute("error", "Киносеанс уже прошёл");
        } catch (SeatAlreadyBookedException exception) {
          model.addAttribute("error", "Вы уже забронировали место на данный сеанс");
        } catch (NoSeatsAvailableException exception) {
            model.addAttribute("error", "Нет свободных мест");
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        }

        model.addAttribute(
                "screening",
                screeningRestClient.findScreeningById(screeningId)
        );
        return "screening-details";
    }

}
