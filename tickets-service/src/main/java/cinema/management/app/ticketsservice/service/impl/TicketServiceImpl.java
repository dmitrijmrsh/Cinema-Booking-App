package cinema.management.app.ticketsservice.service.impl;

import cinema.management.app.ticketsservice.client.ScreeningClient;
import cinema.management.app.ticketsservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.ticketsservice.dto.response.ScreeningDto;
import cinema.management.app.ticketsservice.dto.response.SeatDto;
import cinema.management.app.ticketsservice.dto.response.TicketResponseDto;
import cinema.management.app.ticketsservice.entity.Ticket;
import cinema.management.app.ticketsservice.mapper.TicketMapper;
import cinema.management.app.ticketsservice.repository.TicketRepository;
import cinema.management.app.ticketsservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

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
                        .map(ticketToResponseDto)
                        .toList();

        log.info("Found {} tickets for user with id {}", tickets.size(), userId);

        return tickets;
    }

    @Override
    @Transactional
    public TicketResponseDto saveTicket(final TicketCreationRequestDto dto) {
        TicketResponseDto ticket = ticketToResponseDto.apply(
                ticketRepository.save(ticketMapper.creationDtoToEntity(dto))
        );

        log.info("Saved ticket user {} with screening id {}", dto.userId(), dto.screeningId());

        return ticket;
    }

    @Override
    public Boolean existsByUserIdAndScreeningId(
            final Integer userId,
            final Integer screeningId
    ) {
        return ticketRepository.existsByUserIdAndScreeningId(userId, screeningId);
    }

    @Override
    @Transactional
    public void deleteTicketById(final Integer id) {
        ticketRepository.deleteById(id);
        log.info("Deleted ticket with id {}", id);
    }

    private final Function<Ticket, TicketResponseDto> ticketToResponseDto = new Function<>() {
        @Override
        public TicketResponseDto apply(Ticket ticket) {
            ScreeningDto screening = screeningClient.findScreeningById(
                    ticket.getScreeningId()
            );
            System.out.println("TICKET SEAT ID: " +  ticket.getSeatId());
            SeatDto seat = screeningClient.findSeatById(
                    ticket.getSeatId()
            );
            return new TicketResponseDto(
                    screening.film().title(),
                    screening.date(),
                    screening.time(),
                    screening.hall().id(),
                    seat.rowNumber(),
                    seat.seatInRow()
            );
        }
    };
}
