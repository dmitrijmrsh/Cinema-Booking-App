package cinema.management.app.authservice.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(

        @JsonProperty("refresh_token")
        @NotBlank(message = "{user.data.validation.errors.refresh.token.is.blank}")
        String refreshToken

) {
}
