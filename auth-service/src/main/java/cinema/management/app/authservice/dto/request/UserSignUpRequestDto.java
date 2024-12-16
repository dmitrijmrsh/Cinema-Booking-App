package cinema.management.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignUpRequestDto(

        @NotBlank(message = "{user.data.validation.errors.first.name.is.blank}")
        String firstName,

        @NotBlank(message = "{user.data.validation.errors.last.name.is.blank}")
        String lastName,

        @NotBlank(message = "{user.data.validation.errors.email.is.blank}")
        @Pattern(
                regexp = "^[a-zA-Z0-9.-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "{user.data.validation.errors.email.format.is.invalid}"
        )
        String email,

        @NotBlank(message = "{user.data.validation.errors.password.is.blank}")
        @Size(min = 5, max = 30, message = "{user.data.validation.errors.password.size.is.invalid}")
        String password

) {
}
