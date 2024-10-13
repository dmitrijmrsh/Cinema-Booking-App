package cinema.management.app.filmservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FilmUpdateRequestDto(

        @NotBlank(message = "{film.service.validation.errors.film.title.is.blank}")
        String title,

        @NotBlank(message = "{film.service.validation.errors.film.genre.is.blank}")
        String genre,

        @NotBlank(message = "{film.service.validation.errors.film.description.is.blank}")
        String description,

        @JsonProperty("duration_in_minutes")
        @NotNull(message = "{film.service.validation.errors.film.duration.in.minutes.is.null}")
        Integer durationInMinutes

) {
}
