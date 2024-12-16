package cinema.management.app.ticketsservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record TicketCreationRequestDto(

        @NotNull(message = "{tickets.service.validation.user.id.is.null}")
        Integer userId,

        @NotNull(message = "{ticket.service.validation.screening.id.is.null}")
        Integer screeningId

) {
}
