package cinema.management.app.filmservice.repository.impl;

import cinema.management.app.filmservice.entity.Genre;
import cinema.management.app.filmservice.mapper.row.GenreRowMapper;
import cinema.management.app.filmservice.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GenreRepositoryImpl implements GenreRepository {

    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreRowMapper;

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query(GET_ALL_GENRES, genreRowMapper);
    }

    @Override
    public Optional<Genre> findById(final Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_GENRE_BY_ID, genreRowMapper, id));
    }

    @Override
    public Optional<Genre> findByName(final String name) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_GENRE_BY_NAME, genreRowMapper, name));
    }

    @Override
    public Boolean existsByName(final String name) {
        return jdbcTemplate.queryForObject(EXISTS_BY_NAME, Boolean.class, name);
    }

    @Override
    public Genre save(final Genre genre) {
        return jdbcTemplate.queryForObject(SAVE_GENRE, genreRowMapper, genre.getName());
    }

    private static final String GET_ALL_GENRES = "SELECT * FROM film.get_all_genres()";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM film.get_genre_by_id(?)";
    private static final String GET_GENRE_BY_NAME = "SELECT * FROM film.get_genre_by_name(?)";
    private static final String EXISTS_BY_NAME = "SELECT * FROM film.check_genre_exists_by_name(?)";
    private static final String SAVE_GENRE = "SELECT * FROM film.save_genre(?)";

}
