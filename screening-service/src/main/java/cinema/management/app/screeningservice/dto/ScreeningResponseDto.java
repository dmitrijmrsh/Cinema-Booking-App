package cinema.management.app.screeningservice.dto;

import cinema.management.app.filmservice.entity.Film;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningResponseDto(
        Long id,
        LocalDate date,
        LocalTime time,
        Film film
) {
}
