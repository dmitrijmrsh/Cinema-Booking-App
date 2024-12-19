package cinema.management.app.filmservice.service;

import cinema.management.app.filmservice.dto.request.GenreCreationRequestDto;
import cinema.management.app.filmservice.dto.response.GenreResponseDto;

import java.util.List;

public interface GenreService {

    List<GenreResponseDto> findAllGenres();

    GenreResponseDto findGenreById(Integer id);

    GenreResponseDto saveGenre(GenreCreationRequestDto dto);

}