package cinema.management.app.webclient.client.film;

import cinema.management.app.webclient.dto.film.response.FilmResponseDto;

import java.util.List;

public interface FilmRestClient {

    String FILM_BASE_URI = "http://localhost:8222/api/v1/films";

    List<FilmResponseDto> findAllFilms();

    void createFilm(
            final String title,
            final String genre,
            final String description,
            final Integer durationInMinutes
    );

}
