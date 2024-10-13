package cinema.management.app.filmservice.mapper;

import cinema.management.app.filmservice.dto.request.GenreCreationRequestDto;
import cinema.management.app.filmservice.dto.response.GenreResponseDto;
import cinema.management.app.filmservice.entity.Genre;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface GenreMapper {

    GenreResponseDto entityToDto(Genre genre);

    Genre dtoToEntity(GenreCreationRequestDto dto);

}
