package cinema.management.app.authservice.repository.impl;

import cinema.management.app.authservice.entity.User;
import cinema.management.app.authservice.mapper.row.UserRowMapper;
import cinema.management.app.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userRowMapper;

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(GET_ALL_USERS, userRowMapper);
    }

    @Override
    public Optional<User> findById(final Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_USER_BY_ID, userRowMapper, id));
    }

    @Override
    public Boolean existsById(final Integer id) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ID, Boolean.class, id);
    }

    @Override
    public Boolean existsByEmail(final String email) {
        return jdbcTemplate.queryForObject(EXISTS_BY_EMAIL, Boolean.class, email);
    }

    @Override
    public User save(final User user) {
        return jdbcTemplate.queryForObject(
                SAVE_USER,
                userRowMapper,
                user.getEmail(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole() != null ? user.getRole().getId() : null
        );
    }

    @Override
    public User update(final Integer id, final User user) {
        return jdbcTemplate.queryForObject(
                UPDATE_USER,
                userRowMapper,
                id,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole() != null ? user.getRole().getId() : null
        );
    }

    @Override
    public void deleteById(final Integer id) {
        jdbcTemplate.update(DELETE_USER_BY_ID, id);
    }

    private static final String GET_ALL_USERS = "SELECT * FROM auth.get_all_users()";
    private static final String GET_USER_BY_ID = "SELECT * FROM auth.get_user_by_id(?)";
    private static final String EXISTS_BY_ID = "SELECT * FROM auth.check_user_exists_by_id(?)";
    private static final String EXISTS_BY_EMAIL = "SELECT * FROM auth.check_user_exists_by_email(?)";
    private static final String SAVE_USER = "SELECT * FROM auth.save_user(?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "SELECT * FROM auth.update_user(?, ?, ?, ?, ?)";
    private static final String DELETE_USER_BY_ID = "CALL auth.delete_user_by_id(?)";

}
