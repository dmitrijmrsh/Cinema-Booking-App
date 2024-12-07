package cinema.management.app.filmservice.mapper.row;

import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.entity.Genre;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@NoArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        Genre genre = new Genre();

        film.setId(rs.getInt("id"));
        film.setTitle(rs.getString("title"));
        film.setDescription(rs.getString("description"));
        film.setDurationInMinutes(rs.getInt("duration_in_minutes"));
        genre.setId(rs.getInt("genre_id"));
        genre.setName(rs.getString("genre_name"));
        film.setGenre(genre.getName() != null ? genre : null);

        return film.getTitle() != null && film.getDurationInMinutes() != null
                ? film
                : null;
    }

}
