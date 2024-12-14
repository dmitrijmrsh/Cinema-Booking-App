package cinema.management.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRequestDto(

        @NotBlank(message = "{user.data.validation.errors.first.name.is.blank}")
        String firstName,

        @NotBlank(message = "{user.data.validation.errors.last.name.is.blank}")
        String lastName,

        @NotBlank(message = "{user.data.validation.errors.email.is.blank}")
        @Pattern(
                regexp = "^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "{user.data.validation.errors.email.format.is.invalid}"
        )
        String email

) {
}
