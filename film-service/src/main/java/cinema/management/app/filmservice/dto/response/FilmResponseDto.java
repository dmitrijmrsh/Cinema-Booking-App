package cinema.management.app.filmservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FilmResponseDto(

        Integer id,

        String title,

        String genre,

        String description,

        @JsonProperty("duration_in_minutes")
        Integer durationInMinutes

) {
}
