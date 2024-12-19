package cinema.management.app.webclient.dto.auth.request;

public record UpdateUserRequestDto(
        String firstName,
        String lastName,
        String email
) {
}
