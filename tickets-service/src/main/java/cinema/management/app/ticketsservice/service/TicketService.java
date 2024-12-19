package cinema.management.app.ticketsservice.service;

import cinema.management.app.ticketsservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.ticketsservice.dto.response.TicketResponseDto;

import java.util.List;

public interface TicketService {

    List<TicketResponseDto> findAllTicketsByUserId(final Integer userId);

    TicketResponseDto saveTicket(final TicketCreationRequestDto dto);

    Boolean existsByUserIdAndScreeningId(
            final Integer userId,
            final Integer screeningId
    );

    void deleteTicketById(final Integer id);

    void deleteExpiredTicketsByUserId(final Integer userId);

}
