package cinema.management.app.screeningservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SeatResponseDto(

        Integer id,

        @JsonProperty("row_number")
        Integer rowNumber,

        @JsonProperty("seat_in_row")
        Integer seatInRow,

        @JsonProperty("screening_id")
        Integer screeningId

) {
}
