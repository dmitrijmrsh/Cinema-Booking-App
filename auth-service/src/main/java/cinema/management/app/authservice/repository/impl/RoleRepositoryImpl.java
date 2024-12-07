package cinema.management.app.authservice.repository.impl;

import cinema.management.app.authservice.entity.Role;
import cinema.management.app.authservice.mapper.row.RoleRowMapper;
import cinema.management.app.authservice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RoleRowMapper roleRowMapper;

    @Override
    public Optional<Role> findByName(String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_ROLE_BY_NAME, roleRowMapper, name));
    }

    @Override
    public Boolean existsByName(String name) {
        return jdbcTemplate.queryForObject(EXISTS_BY_NAME, Boolean.class, name);
    }

    private static final String GET_ROLE_BY_NAME = "SELECT * FROM auth.get_role_by_name(?)";
    private static final String EXISTS_BY_NAME = "SELECT * FROM auth.check_role_exists_by_name(?)";
}
