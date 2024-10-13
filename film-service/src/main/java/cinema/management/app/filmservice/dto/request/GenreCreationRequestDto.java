package cinema.management.app.filmservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record GenreCreationRequestDto(

        @NotBlank(message = "{film.service.validation.errors.genre.name.is.blank}")
        String name

) {
}
