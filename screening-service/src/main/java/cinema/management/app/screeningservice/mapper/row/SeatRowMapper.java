package cinema.management.app.screeningservice.mapper.row;

import cinema.management.app.screeningservice.constant.SeatStatus;
import cinema.management.app.screeningservice.entity.Seat;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SeatRowMapper implements RowMapper<Seat> {

    @Override
    public Seat mapRow(ResultSet rs, int rowNum) throws SQLException {
        Seat seat = new Seat();

        seat.setId(rs.getInt("id"));
        seat.setRowNumber(rs.getInt("row_number"));
        seat.setSeatInRow(rs.getInt("seat_in_row"));
        seat.setStatus(SeatStatus.of(rs.getString("status")));
        seat.setScreeningId(rs.getInt("screening_id"));

        return seat;
    }
}
