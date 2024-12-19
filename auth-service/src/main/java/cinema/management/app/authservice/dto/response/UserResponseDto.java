package cinema.management.app.authservice.dto.response;

public record UserResponseDto(

        Integer id,

        String firstName,

        String lastName,

        String email,

        String role

) {
}
