package cinema.management.app.webclient.dto.screening.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningCreationRequestDto(
        LocalDate date,
        LocalTime time,
        Integer filmId,
        Integer hallId
) {
}
