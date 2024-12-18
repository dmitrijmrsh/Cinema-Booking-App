package cinema.management.app.screeningservice.repository.impl;

import cinema.management.app.screeningservice.constant.SeatStatus;
import cinema.management.app.screeningservice.entity.Seat;
import cinema.management.app.screeningservice.mapper.row.SeatRowMapper;
import cinema.management.app.screeningservice.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SeatRowMapper seatRowMapper;

    @Override
    public List<Seat> findAvailableByScreeningId(final Integer screeningId) {
        return jdbcTemplate.query(FIND_AVAILABLE_BY_SCREENING_ID, seatRowMapper, screeningId);
    }

    @Override
    public Optional<Seat> findByScreeningIdAndRowNumberAndSeatInRow(
            final Integer screeningId,
            final Integer rowNumber,
            final Integer seatInRow
    ) {
        Seat seat;

        try {
            seat = jdbcTemplate.queryForObject(
                    FIND_BY_SCREENING_ID_AND_ROW_NUMBER_AND_SEAT_IN_ROW,
                    seatRowMapper,
                    screeningId,
                    rowNumber,
                    seatInRow
            );
        } catch (DataAccessException exception) {
            seat = null;
        }

        return Optional.ofNullable(seat);
    }

    @Override
    public Optional<Seat> findById(final Integer id) {
        Seat seat;

        try {
            seat = jdbcTemplate.queryForObject(FIND_BY_ID, seatRowMapper, id);
        } catch (DataAccessException exception) {
            seat = null;
        }

        return Optional.ofNullable(seat);
    }

    @Override
    public Seat save(final Seat seat) {
        return jdbcTemplate.queryForObject(
                SAVE_SEAT,
                seatRowMapper,
                seat.getRowNumber(),
                seat.getSeatInRow(),
                seat.getStatus().getSeatStatusInStr(),
                seat.getScreeningId()
        );
    }

    @Override
    public Seat updateStatus(final Integer id, final SeatStatus status) {
        return jdbcTemplate.queryForObject(
                UPDATE_SEAT_STATUS,
                seatRowMapper,
                id,
                status.getSeatStatusInStr()
        );
    }

    @Override
    public void deleteById(final Integer id) {
        jdbcTemplate.update(DELETE_SEAT_BY_ID, id);
    }

    private static final String FIND_AVAILABLE_BY_SCREENING_ID = "SELECT * FROM screening.get_available_by_screening_id(?)";
    private static final String FIND_BY_SCREENING_ID_AND_ROW_NUMBER_AND_SEAT_IN_ROW =
            "SELECT * FROM screening.get_by_screening_id_and_row_number_and_seat_in_row(?, ?, ?)";
    private static final String FIND_BY_ID = "SELECT * FROM screening.get_by_id(?)";
    private static final String SAVE_SEAT = "SELECT * FROM screening.save_seat(?, ?, ?, ?)";
    private static final String UPDATE_SEAT_STATUS = "SELECT * FROM screening.update_seat_status(?, ?)";
    private static final String DELETE_SEAT_BY_ID = "CALL screening.delete_seat_by_id(?)";
}
