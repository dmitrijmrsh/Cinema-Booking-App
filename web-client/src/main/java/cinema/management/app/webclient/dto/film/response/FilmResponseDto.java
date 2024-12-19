package cinema.management.app.webclient.dto.film.response;

public record FilmResponseDto(

        Integer id,

        String title,

        String genre,

        String description,

        Integer durationInMinutes

) {
}
