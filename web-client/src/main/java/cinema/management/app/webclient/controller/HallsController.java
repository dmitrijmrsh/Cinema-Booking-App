package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.hall.HallRestClient;
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
@RequestMapping("/profile/special-features/halls")
@RequiredArgsConstructor
public class HallsController {

    private final HallRestClient hallRestClient;

    @GetMapping
    public String getHallsPage(Model model) {
        try {
            model.addAttribute(
                    "halls",
                    hallRestClient.findAllHalls()
            );
            return "halls";
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        } catch (AccessDeniedException exception) {
            return "redirect:/";
        }
    }

    @GetMapping("/create")
    public String getCreateHallPage() {
        return "create-hall";
    }

    @PostMapping("/create")
    public String createHall(
            @RequestParam final Integer rowNumber,
            @RequestParam final Integer seatInRowCount,
            Model model
    ) {
        try {
            hallRestClient.createHall(
                    rowNumber,
                    seatInRowCount
            );
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        } catch (BadRequestException exception) {
            model.addAttribute(
                    "error",
                    "Некорректные данные для зала"
            );
            return "create-hall";
        } catch (AccessDeniedException exception) {
            return "redirect:/";
        }

        model.addAttribute(
                "halls",
                hallRestClient.findAllHalls()
        );
        return "halls";
    }
}
