package cinema.management.app.authservice.mapper.row;

import cinema.management.app.authservice.entity.Role;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
public class RoleRowMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
        Role role = new Role();

        role.setId(rs.getInt("id"));
        role.setName(rs.getString("name"));

        return role.getId() != 0 && role.getName() != null
                ? role
                : null;
    }
}
