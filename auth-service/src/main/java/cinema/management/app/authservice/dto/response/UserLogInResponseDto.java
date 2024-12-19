package cinema.management.app.authservice.dto.response;

public record UserLogInResponseDto(

        String accessToken,

        String refreshToken

) {
}
