package cinema.management.app.screeningservice.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record ScreeningRequestDto(

        @NotNull(message = "{screening.service.data.validation.date.is.null}")
        @FutureOrPresent(message = "{screening.service.data.validation.date.is.in.past}")
        LocalDate date,

        @NotNull(message = "{screening.service.data.validation.time.is.null}")
        LocalTime time
) {
}