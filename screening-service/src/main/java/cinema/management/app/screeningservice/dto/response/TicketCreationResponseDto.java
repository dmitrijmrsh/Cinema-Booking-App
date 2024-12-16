package cinema.management.app.screeningservice.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record TicketCreationResponseDto(

        String filmTitle,

        LocalDate date,

        LocalTime time,

        Integer hallId

) {
}
