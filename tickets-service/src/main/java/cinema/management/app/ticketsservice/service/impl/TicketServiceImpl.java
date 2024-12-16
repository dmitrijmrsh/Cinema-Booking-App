package cinema.management.app.ticketsservice.service.impl;

import cinema.management.app.ticketsservice.client.ScreeningClient;
import cinema.management.app.ticketsservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.ticketsservice.dto.response.TicketResponseDto;
import cinema.management.app.ticketsservice.mapper.TicketMapper;
import cinema.management.app.ticketsservice.repository.TicketRepository;
import cinema.management.app.ticketsservice.service.TicketService;
import com.netflix.discovery.converters.Auto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ScreeningClient screeningClient;
    private final TicketMapper ticketMapper;

    @Override
    public List<TicketResponseDto> findAllTicketsByUserId(final Integer userId) {
        List<TicketResponseDto> tickets = ticketRepository.findAllByUserId(userId).stream()
                .map(ticket -> ticketMapper.screeningDtoToResponseDto(
                        screeningClient.findScreeningById(ticket.getScreeningId())
                ))
                .toList();

        log.info("Found {} tickets for user with id {}", tickets.size(), userId);

        return tickets;
    }

    @Override
    public TicketResponseDto saveTicket(final TicketCreationRequestDto dto) {
        TicketResponseDto ticket = ticketMapper.screeningDtoToResponseDto(
                screeningClient.findScreeningById(
                    ticketRepository.save(
                            ticketMapper.creationDtoToEntity(dto)
                    ).getScreeningId()
                )
        );

        log.info("Saved ticket user {} with screening id {}", dto.userId(), dto.screeningId());

        return ticket;
    }

    @Override
    public void deleteTicketById(final Integer id) {
        ticketRepository.deleteById(id);
        log.info("Deleted ticket with id {}", id);
    }
}
