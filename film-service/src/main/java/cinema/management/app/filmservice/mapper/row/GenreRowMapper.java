package cinema.management.app.filmservice.mapper.row;

import cinema.management.app.filmservice.entity.Genre;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
public class GenreRowMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("id"));
        genre.setName(rs.getString("name"));
        return genre.getName() != null ? genre : null;
    }

}
