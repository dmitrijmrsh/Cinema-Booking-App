package cinema.management.app.screeningservice.repository.impl;

import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.mapper.row.ScreeningRowMapper;
import cinema.management.app.screeningservice.repository.ScreeningRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScreeningRepositoryImpl implements ScreeningRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ScreeningRowMapper screeningRowMapper;

    @Override
    public List<Screening> findAll() {
        return jdbcTemplate.query(GET_ALL_SCREENINGS, screeningRowMapper);
    }

    @Override
    public List<Screening> findAllByDate(final Date date) {
        return jdbcTemplate.query(GET_SCREENINGS_BY_DATE, screeningRowMapper, date);
    }

    @Override
    public List<Screening> findAllByHallIdAndDate(Integer hallId, LocalDate date) {
        return jdbcTemplate.query(
                GET_SCREENING_BY_HALL_ID_AND_DATE,
                screeningRowMapper,
                hallId,
                date
        );
    }

    @Override
    public List<Screening> findAllByFilmId(final Integer filmId) {
        return jdbcTemplate.query(GET_SCREENINGS_BY_FILM_ID, screeningRowMapper, filmId);
    }

    @Override
    public Optional<Screening> findById(final Integer id) {
        Screening screening;

        try {
            screening = jdbcTemplate.queryForObject(GET_SCREENING_BY_ID, screeningRowMapper, id);
        } catch (DataAccessException e) {
            screening = null;
        }

        return Optional.ofNullable(screening);
    }

    @Override
    public Screening save(final Screening screening) {
        return jdbcTemplate.queryForObject(
                SAVE_SCREENING,
                screeningRowMapper,
                Date.valueOf(screening.getDate()),
                Time.valueOf(screening.getTime()),
                screening.getFilmId(),
                screening.getHallId()
        );
    }

    @Override
    public void deleteById(final Integer id) {
        jdbcTemplate.update(DELETE_SCREENING_BY_ID, id);
    }

    private static final String GET_ALL_SCREENINGS = "SELECT * FROM screening.get_all_screenings()";
    private static final String GET_SCREENINGS_BY_DATE = "SELECT * FROM screening.get_screenings_by_date(?)";
    private static final String GET_SCREENING_BY_HALL_ID_AND_DATE =
            "SELECT * FROM screening.get_all_screenings_by_hall_id_and_date(?, ?)";
    private static final String GET_SCREENINGS_BY_FILM_ID = "SELECT * FROM screening.get_screenings_by_film_id(?)";
    private static final String GET_SCREENING_BY_ID = "SELECT * FROM screening.get_screening_by_id(?)";
    private static final String SAVE_SCREENING = "SELECT * FROM screening.save_screening(?, ?, ?, ?)";
    private static final String DELETE_SCREENING_BY_ID = "CALL screening.delete_screening_by_id(?)";
}
