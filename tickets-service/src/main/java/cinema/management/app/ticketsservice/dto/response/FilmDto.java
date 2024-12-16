package cinema.management.app.ticketsservice.dto.response;

public record FilmDto(

        Integer id,

        String title,

        String genre,

        String description,

        Integer durationInMinutes

) {
}
