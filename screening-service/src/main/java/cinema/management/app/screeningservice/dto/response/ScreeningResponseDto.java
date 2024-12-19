package cinema.management.app.screeningservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ScreeningResponseDto(

        Integer id,

        LocalDate date,

        LocalTime time,

        @JsonProperty("film")
        FilmDto filmDto,

        @JsonProperty("hall")
        HallDto hallDto,

        List<SeatResponseDto> seats
) {
}
