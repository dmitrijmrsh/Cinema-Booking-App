package cinema.management.app.webclient.dto.auth.response;

public record UserResponseDto(
        Integer id,
        String firstName,
        String lastName,
        String email,
        String role
) {
}
