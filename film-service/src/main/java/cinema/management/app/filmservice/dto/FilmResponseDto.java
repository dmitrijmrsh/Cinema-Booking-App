package cinema.management.app.filmservice.dto;

import cinema.management.app.filmservice.entity.enums.FilmCategory;

public record FilmResponseDto(
        Long id,
        String title,
        FilmCategory category,
        Integer durationFilmInMinutes
) {
}
