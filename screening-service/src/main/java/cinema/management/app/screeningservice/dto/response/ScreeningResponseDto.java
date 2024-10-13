package cinema.management.app.screeningservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningResponseDto(

        Integer id,

        LocalDate date,

        LocalTime time,

        @JsonProperty("hall")
        FilmDto filmDto,

        @JsonProperty("film")
        HallDto hallDto

) {
}
