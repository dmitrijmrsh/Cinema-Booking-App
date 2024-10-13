package cinema.management.app.screeningservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningCreationRequestDto(

        @NotNull(message = "{screening.service.data.validation.date.is.null}")
        @FutureOrPresent(message = "{screening.service.data.validation.date.is.in.past}")
        LocalDate date,

        @NotNull(message = "{screening.service.data.validation.time.is.null}")
        LocalTime time,

        @JsonProperty("film_id")
        @NotNull(message = "{screening.service.data.validation.film.id.is.null}")
        Integer filmId,

        @JsonProperty("hall_id")
        @NotNull(message = "{screening.service.data.validation.hall.id.is.null}")
        Integer hallId

) {
}
