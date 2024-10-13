package cinema.management.app.filmservice.service;

import cinema.management.app.filmservice.dto.request.FilmCreationRequestDto;
import cinema.management.app.filmservice.dto.request.FilmUpdateRequestDto;
import cinema.management.app.filmservice.dto.response.FilmResponseDto;

import java.util.List;

public interface FilmService {

    List<FilmResponseDto> findAllFilms();

    List<FilmResponseDto> findFilmByGenre(String genreName);

    FilmResponseDto findFilmById(Integer id);

    FilmResponseDto saveFilm(FilmCreationRequestDto dto);

    FilmResponseDto updateFilm(Integer id, FilmUpdateRequestDto dto);

    void deleteFilm(Integer id);

}
