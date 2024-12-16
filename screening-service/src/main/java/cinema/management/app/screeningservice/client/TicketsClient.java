package cinema.management.app.screeningservice.client;

import cinema.management.app.screeningservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.screeningservice.dto.response.TicketCreationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tickets-service", url = "${application.config.tickets-url}")
public interface TicketsClient {

    @PostMapping
    TicketCreationResponseDto createTicket(
            @RequestBody TicketCreationRequestDto dto
    );

}
