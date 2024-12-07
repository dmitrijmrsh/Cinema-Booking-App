package cinema.management.app.authservice.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserResponseDto(

        Integer id,

        @JsonProperty("first_name")
        String firstName,

        @JsonProperty("last_name")
        String lastName,

        String email,

        String role

) {
}
