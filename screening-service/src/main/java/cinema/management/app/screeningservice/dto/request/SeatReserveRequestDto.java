package cinema.management.app.screeningservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record SeatReserveRequestDto(

        @JsonProperty("row_number")
        @NotNull(message = "{screening.service.data.validation.rows.number.is.null}")
        Integer rowNumber,

        @JsonProperty("seat_in_row")
        @NotNull(message = "{screening.service.data.validation.seat.in.row.is.null}")
        Integer seatInRow

) {
}
