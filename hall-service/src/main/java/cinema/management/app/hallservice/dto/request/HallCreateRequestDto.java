package cinema.management.app.hallservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record HallCreateRequestDto(

        @JsonProperty("is_active")
        @NotNull(message = "{hall.service.data.validation.hall.activity.field.is.null}")
        Boolean isActive,

        @JsonProperty("row_count")
        @NotNull(message = "{hall.service.data.validation.hall.row.count.field.is.null}")
        @Positive(message = "{hall.service.data.validation.hall.row.count.field.is.negative.or.zero}")
        Integer rowCount,

        @JsonProperty("seat_in_row_count")
        @NotNull(message = "{hall.service.data.validation.hall.seat.in.row.count.field.is.null}")
        @Positive(message = "{hall.service.data.validation.hall.row.count.field.is.negative.or.zero}")
        Integer seatInRowCount

) {
}
