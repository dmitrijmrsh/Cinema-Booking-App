package cinema.management.app.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AccessTokenResponseDto(

        @JsonProperty("access_token")
        String accessToken

) {
}
