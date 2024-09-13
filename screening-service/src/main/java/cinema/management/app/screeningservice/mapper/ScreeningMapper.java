package cinema.management.app.screeningservice.mapper;

import cinema.management.app.screeningservice.dto.ScreeningAvailableSeatsDto;
import cinema.management.app.screeningservice.dto.ScreeningRequestDto;
import cinema.management.app.screeningservice.entity.Screening;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface ScreeningMapper {

    Screening dtoToEntity(ScreeningRequestDto screeningRequestDto);

    ScreeningAvailableSeatsDto entityToSeatsDto(Screening screening);
}
