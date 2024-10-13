package cinema.management.app.screeningservice.mapper;

import cinema.management.app.screeningservice.dto.response.SeatResponseDto;
import cinema.management.app.screeningservice.entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SeatMapper {

    @Mapping(source = "seat.screening.id", target = "screeningId")
    SeatResponseDto entityToDto(Seat seat);

}
