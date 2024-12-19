package cinema.management.app.webclient.dto.auth.response;

public record UserLogInResponseDto(
        String accessToken,
        String refreshToken
) {
}
