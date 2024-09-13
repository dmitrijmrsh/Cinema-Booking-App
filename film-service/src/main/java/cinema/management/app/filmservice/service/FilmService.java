package cinema.management.app.filmservice.service;

import cinema.management.app.filmservice.dto.CreatedFilmDto;
import cinema.management.app.filmservice.dto.FilmRequestDto;
import cinema.management.app.filmservice.dto.FilmResponseDto;
import cinema.management.app.filmservice.entity.enums.FilmCategory;

import java.util.List;

public interface FilmService {

    CreatedFilmDto save(FilmRequestDto filmRequestDto);

    FilmResponseDto findById(Long id);

    List<FilmResponseDto> findAll();

    List<FilmResponseDto> findByCategory(FilmCategory filmCategory);

    void delete(Long id);
}
