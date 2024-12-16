package cinema.management.app.ticketsservice.repository.impl;

import cinema.management.app.ticketsservice.entity.Ticket;
import cinema.management.app.ticketsservice.mapper.row.TicketRowMapper;
import cinema.management.app.ticketsservice.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TicketRepositoryImpl implements TicketRepository {

    private final JdbcTemplate jdbcTemplate;
    private final TicketRowMapper ticketRowMapper;

    @Override
    public List<Ticket> findAllByUserId(final Integer userId) {
        return jdbcTemplate.query(GET_ALL_TICKETS_BY_USER_ID, ticketRowMapper, userId);
    }

    @Override
    public Ticket save(final Ticket ticket) {
        return jdbcTemplate.queryForObject(
                SAVE_TICKET,
                ticketRowMapper,
                ticket.getUserId(),
                ticket.getScreeningId()
        );
    }

    @Override
    public void deleteById(final Integer id) {
        jdbcTemplate.update(DELETE_TICKET_BY_ID, id);
    }

    private static final String GET_ALL_TICKETS_BY_USER_ID = "SELECT * FROM ticket.get_all_by_user_id(?)";
    private static final String SAVE_TICKET = "SELECT * FROM ticket.save_ticket(?, ?)";
    private static final String DELETE_TICKET_BY_ID = "CALL ticket.delete_ticket_by_id(?)";

}
