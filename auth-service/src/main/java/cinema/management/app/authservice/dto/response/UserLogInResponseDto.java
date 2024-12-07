package cinema.management.app.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserLogInResponseDto(

        @JsonProperty("access_token")
        String accessToken,

        @JsonProperty("refresh_token")
        String refreshToken

) {
}
