package cinema.management.app.filmservice.mapper;

import cinema.management.app.filmservice.dto.request.FilmCreationRequestDto;
import cinema.management.app.filmservice.dto.request.FilmUpdateRequestDto;
import cinema.management.app.filmservice.dto.response.FilmResponseDto;
import cinema.management.app.filmservice.entity.Film;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FilmMapper {

    @Mapping(source = "genre.name", target = "genre")
    FilmResponseDto entityToDto(Film film);

    @Mapping(source = "genre", target = "genre.name")
    Film dtoToEntity(FilmCreationRequestDto filmCreationRequestDto);

    @Mapping(source = "genre", target = "genre.name")
    Film dtoToEntity(FilmUpdateRequestDto filmUpdateRequestDto);

}
