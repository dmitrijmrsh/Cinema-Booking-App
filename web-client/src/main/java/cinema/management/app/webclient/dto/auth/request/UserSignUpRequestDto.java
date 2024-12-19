package cinema.management.app.webclient.dto.auth.request;

public record UserSignUpRequestDto(
        String firstName,
        String lastName,
        String email,
        String password
) {
}
