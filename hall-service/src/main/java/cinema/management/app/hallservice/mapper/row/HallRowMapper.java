package cinema.management.app.hallservice.mapper.row;

import cinema.management.app.hallservice.entity.Hall;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
public class HallRowMapper implements RowMapper<Hall> {

    @Override
    public Hall mapRow(ResultSet rs, int rowNum) throws SQLException {
        Hall hall = new Hall();

        hall.setId(rs.getInt("id"));
        hall.setIsActive(rs.getBoolean("is_active"));
        hall.setRowCount(rs.getInt("row_count"));
        hall.setSeatInRowCount(rs.getInt("seat_in_row_count"));

        return hall.getId() != 0 &&
               hall.getIsActive() != null &&
               hall.getRowCount() != null &&
               hall.getSeatInRowCount() != null
               ? hall
               : null;
    }
}
