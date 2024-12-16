package cinema.management.app.ticketsservice.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningDto(

        LocalDate date,

        LocalTime time,

        FilmDto film,

        HallDto hall

) {
}
