package cinema.management.app.screeningservice.dto.response;

public record UserDto(

        Integer id,

        String firstName,

        String lastName,

        String email,

        String role

) {
}
