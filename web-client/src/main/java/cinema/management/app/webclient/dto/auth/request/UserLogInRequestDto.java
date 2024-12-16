package cinema.management.app.webclient.dto.auth.request;

public record UserLogInRequestDto(
        String email,
        String password
) {
}
