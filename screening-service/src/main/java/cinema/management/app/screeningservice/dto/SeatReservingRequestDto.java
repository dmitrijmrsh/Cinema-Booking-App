package cinema.management.app.screeningservice.dto;

import jakarta.validation.constraints.NotNull;

public record SeatReservingRequestDto(

        @NotNull(message = "{screening.service.data.validation.screening.id.is.null}")
        Long screeningId,

        @NotNull(message = "{screening.service.data.validation.rows.number.is.null}")
        Integer rowsNumber,

        @NotNull(message = "{screening.service.data.validation.seat.in.row}")
        Integer seatInRow
) {
}
