package cinema.management.app.authservice.mapper.row;

import cinema.management.app.authservice.entity.Role;
import cinema.management.app.authservice.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        Role role = new Role();

        user.setId(rs.getInt("id"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        role.setId(rs.getInt("role_id"));
        role.setName(rs.getString("role_name"));
        user.setRole(role.getId() != 0 && role.getName() != null ? role : null);


        return user.getId() != 0 &&
               user.getEmail() != null
               ? user
               : null;
    }
}
