package cinema.management.app.webclient.dto.tickets.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record TicketResponseDto(
        String filmTitle,
        LocalDate date,
        LocalTime time,
        Integer hallId,
        Integer rowNumber,
        Integer seatInRow
) {
}
