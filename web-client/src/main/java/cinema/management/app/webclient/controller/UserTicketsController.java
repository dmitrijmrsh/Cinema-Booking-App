package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.tickets.TicketsRestClient;
import cinema.management.app.webclient.exception.UserNotFoundException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile/tickets")
@RequiredArgsConstructor
public class UserTicketsController {

    private final TicketsRestClient ticketsRestClient;

    @GetMapping
    public String getTicketsList(Model model) {
        try {
            model.addAttribute(
                    "tickets",
                    ticketsRestClient.findAllTicketsForCurrentUser()
            );
            return "tickets";
        } catch (UserUnauthorizedException | UserNotFoundException exception) {
            return "redirect:/login";
        }
    }

    @GetMapping("/expired")
    public String deleteExpiredTicketsForCurrentUser(Model model) {
        try {
            ticketsRestClient.deleteAllExpiredTicketsForCurrentUser();
            model.addAttribute(
                    "tickets",
                    ticketsRestClient.findAllTicketsForCurrentUser()
            );
            return "tickets";
        } catch (UserUnauthorizedException | UserNotFoundException exception) {
            return "redirect:/login";
        }
    }

}
