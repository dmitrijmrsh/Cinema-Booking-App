package cinema.management.app.webclient.dto.film.request;

public record FilmCreationRequestDto(
        String title,
        String genre,
        String description,
        Integer durationInMinutes
) {
}
