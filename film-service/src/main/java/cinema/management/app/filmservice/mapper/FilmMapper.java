package cinema.management.app.filmservice.mapper;

import cinema.management.app.filmservice.dto.CreatedFilmDto;
import cinema.management.app.filmservice.dto.FilmRequestDto;
import cinema.management.app.filmservice.dto.FilmResponseDto;
import cinema.management.app.filmservice.entity.Film;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FilmMapper {

    FilmResponseDto entityToDto(Film film);

    CreatedFilmDto createdEntityToDto(Film film);

    Film dtoToEntity(FilmRequestDto filmRequestDto);
}
