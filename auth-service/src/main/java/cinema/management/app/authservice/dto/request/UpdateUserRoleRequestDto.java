package cinema.management.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UpdateUserRoleRequestDto(

        @NotBlank(message = "{user.data.validation.errors.role.is.blank}")
        @Pattern(
                regexp = "ROLE_MANAGER|ROLE_USER|MANAGER|USER",
                message = "{user.data.validation.errors.role.does.not.exist}"
        )
        String role

) {
}
