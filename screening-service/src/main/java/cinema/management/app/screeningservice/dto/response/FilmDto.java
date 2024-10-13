package cinema.management.app.screeningservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FilmDto(

        Integer id,

        String title,

        String genre,

        String description,

        @JsonProperty("duration_in_minutes")
        Integer durationInMinutes

) {
}
