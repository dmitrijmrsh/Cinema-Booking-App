package cinema.management.app.webclient.dto.screening.response;

import cinema.management.app.webclient.dto.film.response.FilmResponseDto;
import cinema.management.app.webclient.dto.hall.response.HallResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScreeningResponseDto(

        Integer id,

        LocalDate date,

        LocalTime time,

        FilmResponseDto film,

        HallResponseDto hall,

        List<SeatResponseDto> seats
) {
}
