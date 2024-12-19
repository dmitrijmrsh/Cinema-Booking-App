package cinema.management.app.hallservice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record HallCreateRequestDto(

        @NotNull(message = "{hall.service.data.validation.hall.activity.field.is.null}")
        Boolean isActive,

        @NotNull(message = "{hall.service.data.validation.hall.row.count.field.is.null}")
        @Positive(message = "{hall.service.data.validation.hall.row.count.field.is.negative.or.zero}")
        Integer rowCount,

        @NotNull(message = "{hall.service.data.validation.hall.seat.in.row.count.field.is.null}")
        @Positive(message = "{hall.service.data.validation.hall.row.count.field.is.negative.or.zero}")
        Integer seatInRowCount

) {
}
