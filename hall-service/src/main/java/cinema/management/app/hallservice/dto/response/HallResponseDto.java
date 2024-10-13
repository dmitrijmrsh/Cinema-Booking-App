package cinema.management.app.hallservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HallResponseDto(

        Integer id,

        @JsonProperty("is_active")
        Boolean isActive,

        @JsonProperty("row_count")
        Integer rowCount,

        @JsonProperty("seat_in_row_count")
        Integer seatInRowCount


) {
}
