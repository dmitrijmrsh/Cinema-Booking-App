package cinema.management.app.filmservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilmRequestDto(

        @NotBlank(message = "{film.service.validation.errors.title.is.blank}")
        String title,

        @NotNull(message = "{film.service.validation.errors.film.category.is.null}")
        String category,

        @NotNull(message = "{film.service.validation.errors.duration.in.minutes.is.null}")
        Integer durationFilmInMinutes
) {
}
