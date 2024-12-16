package cinema.management.app.ticketsservice.mapper;

import cinema.management.app.ticketsservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.ticketsservice.dto.response.ScreeningDto;
import cinema.management.app.ticketsservice.dto.response.TicketResponseDto;
import cinema.management.app.ticketsservice.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TicketMapper {

    Ticket creationDtoToEntity(TicketCreationRequestDto dto);

    @Mapping(target = "filmTitle", source = "film.title")
    @Mapping(target = "hallId", source = "hall.id")
    TicketResponseDto screeningDtoToResponseDto(ScreeningDto dto);

}
