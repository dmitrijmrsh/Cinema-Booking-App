package cinema.management.app.hallservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

public record HallUpdateActivityStatusRequestDto(

        @JsonProperty("is_active")
        @NotNull(message = "{hall.service.data.validation.hall.activity.field.is.null}")
        Boolean isActive

) {
}
