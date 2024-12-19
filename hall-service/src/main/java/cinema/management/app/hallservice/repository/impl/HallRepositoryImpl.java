package cinema.management.app.hallservice.repository.impl;

import cinema.management.app.hallservice.entity.Hall;
import cinema.management.app.hallservice.mapper.row.HallRowMapper;
import cinema.management.app.hallservice.repository.HallRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class HallRepositoryImpl implements HallRepository {

    private final JdbcTemplate jdbcTemplate;
    private final HallRowMapper hallRowMapper;

    @Override
    public List<Hall> findAll() {
        return jdbcTemplate.query(GET_ALL_HALLS, hallRowMapper);
    }

    @Override
    public List<Hall> findHallsByIsActive(final Boolean isActive) {
        return jdbcTemplate.query(GET_HALLS_BY_IS_ACTIVE, hallRowMapper, isActive);
    }

    @Override
    public Optional<Hall> findById(final Integer id) {
        Hall hall;

        try {
            hall = jdbcTemplate.queryForObject(GET_HALL_BY_ID, hallRowMapper, id);
        } catch (DataAccessException exception) {
            hall = null;
        }

        return Optional.ofNullable(hall);
    }

    @Override
    public Hall save(final Hall hall) {
        return jdbcTemplate.queryForObject(
                SAVE_HALL,
                hallRowMapper,
                hall.getIsActive(),
                hall.getRowCount(),
                hall.getSeatInRowCount()
        );
    }

    @Override
    public Hall update(final Integer id, final Hall hall) {
        return jdbcTemplate.queryForObject(
                UPDATE_HALL,
                hallRowMapper,
                id,
                hall.getIsActive(),
                hall.getRowCount(),
                hall.getSeatInRowCount()
        );
    }

    @Override
    public void deleteById(final Integer id) {
        jdbcTemplate.update(DELETE_HALL_BY_ID, id);
    }

    private static final String GET_ALL_HALLS = "SELECT * FROM hall.get_all_halls()";
    private static final String GET_HALLS_BY_IS_ACTIVE = "SELECT * FROM hall.get_all_halls_by_activity(?)";
    private static final String GET_HALL_BY_ID = "SELECT * FROM hall.get_hall_by_id(?)";
    private static final String SAVE_HALL = "SELECT * FROM hall.save_hall(?, ?, ?)";
    private static final String UPDATE_HALL = "SELECT * FROM hall.update_hall(?, ?, ?, ?)";
    private static final String DELETE_HALL_BY_ID = "CALL hall.delete_hall_by_id(?)";

}
