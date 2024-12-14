package cinema.management.app.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(

        @NotBlank(message = "{user.data.validation.errors.refresh.token.is.blank}")
        String refreshToken

) {
}
