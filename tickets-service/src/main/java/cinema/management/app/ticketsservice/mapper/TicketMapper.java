package cinema.management.app.ticketsservice.mapper;

import cinema.management.app.ticketsservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.ticketsservice.entity.Ticket;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TicketMapper {

    Ticket creationDtoToEntity(TicketCreationRequestDto dto);

}
