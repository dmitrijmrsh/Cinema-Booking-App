package cinema.management.app.filmservice.service;

import cinema.management.app.filmservice.dto.request.FilmCreationRequestDto;
import cinema.management.app.filmservice.dto.request.FilmUpdateRequestDto;
import cinema.management.app.filmservice.dto.response.FilmResponseDto;

import java.util.List;

public interface FilmService {

    List<FilmResponseDto> findAllFilms();

    List<FilmResponseDto> findFilmByGenre(final String genreName);

    FilmResponseDto findFilmById(final Integer id);

    FilmResponseDto saveFilm(final FilmCreationRequestDto dto);

    FilmResponseDto updateFilm(final Integer id, final FilmUpdateRequestDto dto);

    void deleteFilm(final Integer id);

}
