package cinema.management.app.screeningservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record SeatReserveRequestDto(

        @NotNull(message = "{screening.service.data.validation.rows.number.is.null}")
        Integer rowNumber,

        @NotNull(message = "{screening.service.data.validation.seat.in.row.is.null}")
        Integer seatInRow

) {
}
