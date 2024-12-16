package cinema.management.app.ticketsservice.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record TicketResponseDto(

        String filmTitle,

        LocalDate date,

        LocalTime time,

        Integer hallId

) {
}
