package cinema.management.app.ticketsservice.mapper.row;

import cinema.management.app.ticketsservice.entity.Ticket;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TicketRowMapper implements RowMapper<Ticket> {

    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();

        ticket.setId(rs.getInt("id"));
        ticket.setScreeningId(rs.getInt("screening_id"));
        ticket.setUserId(rs.getInt("user_id"));

        return ticket;
    }
}
