package cinema.management.app.screeningservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HallDto(

        Integer id,

        @JsonProperty("is_active")
        Boolean isActive,

        @JsonProperty("row_count")
        Integer rowCount,

        @JsonProperty("seat_in_row_count")
        Integer seatInRowCount

) {
}
