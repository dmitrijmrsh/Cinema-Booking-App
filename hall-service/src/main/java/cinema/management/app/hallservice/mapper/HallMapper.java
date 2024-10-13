package cinema.management.app.hallservice.mapper;

import cinema.management.app.hallservice.dto.request.HallCreateRequestDto;
import cinema.management.app.hallservice.dto.response.HallResponseDto;
import cinema.management.app.hallservice.entity.Hall;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface HallMapper {

    Hall dtoToEntity(HallCreateRequestDto hallCreateRequestDto);

    HallResponseDto entityToDto(Hall hall);

}
