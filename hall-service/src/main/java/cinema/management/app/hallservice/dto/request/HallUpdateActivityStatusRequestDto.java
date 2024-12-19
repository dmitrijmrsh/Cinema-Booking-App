package cinema.management.app.hallservice.dto.request;

import jakarta.validation.constraints.NotNull;

public record HallUpdateActivityStatusRequestDto(

        @NotNull(message = "{hall.service.data.validation.hall.activity.field.is.null}")
        Boolean isActive

) {
}
