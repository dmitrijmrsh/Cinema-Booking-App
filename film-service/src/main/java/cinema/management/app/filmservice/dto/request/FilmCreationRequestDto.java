package cinema.management.app.filmservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record FilmCreationRequestDto(

        @NotBlank(message = "{film.service.validation.errors.film.title.is.blank}")
        String title,

        @NotBlank(message = "{film.service.validation.errors.film.genre.is.blank}")
        String genre,

        @NotBlank(message = "{film.service.validation.errors.film.description.is.blank}")
        String description,

        @Positive
        @NotNull(message = "{film.service.validation.errors.film.duration.in.minutes.is.null}")
        Integer durationInMinutes

) {
}
