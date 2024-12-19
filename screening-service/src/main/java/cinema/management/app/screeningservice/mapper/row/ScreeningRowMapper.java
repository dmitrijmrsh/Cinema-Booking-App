package cinema.management.app.screeningservice.mapper.row;

import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.entity.Seat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ScreeningRowMapper implements RowMapper<Screening> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Screening mapRow(ResultSet rs, int rowNum) throws SQLException {
        Screening screening = new Screening();

        screening.setId(rs.getInt("id"));
        screening.setDate(rs.getObject("start_date", LocalDate.class));
        screening.setTime(rs.getObject("start_time", LocalTime.class));
        screening.setFilmId(rs.getInt("film_id"));
        screening.setHallId(rs.getInt("hall_id"));

        String seatsJson = rs.getString("seats");
        List<Seat> seats = null;

        if (seatsJson != null && !seatsJson.isEmpty()) {
            try {
                seats = objectMapper.readValue(seatsJson, new TypeReference<List<Seat>>() {});
            } catch (Exception ex) {
                throw new SQLException("Error parsing JSON for seats", ex);
            }
        }

        screening.setSeats(seats);

        return screening;
    }
}
