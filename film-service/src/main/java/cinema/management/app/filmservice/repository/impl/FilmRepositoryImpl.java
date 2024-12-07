package cinema.management.app.filmservice.repository.impl;

import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.mapper.row.FilmRowMapper;
import cinema.management.app.filmservice.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FilmRepositoryImpl implements FilmRepository {

    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmRowMapper;

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query(GET_ALL_FILMS, filmRowMapper);
    }

    @Override
    public List<Film> findAllByGenreName(final String genreName) {
        return jdbcTemplate.query(GET_FILMS_BY_GENRE_NAME, filmRowMapper, genreName);
    }

    @Override
    public Optional<Film> findById(final Integer id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_FILM_BY_ID, filmRowMapper, id));
    }

    @Override
    public Boolean existsById(final Integer id) {
        return jdbcTemplate.queryForObject(EXISTS_BY_ID, Boolean.class, id);
    }

    @Override
    public Boolean existsByTitle(final String name) {
        return jdbcTemplate.queryForObject(EXISTS_BY_TITLE, Boolean.class, name);
    }

    @Override
    public Film save(final Film film) {
        return jdbcTemplate.queryForObject(
                SAVE_FILM,
                filmRowMapper,
                film.getTitle(),
                film.getDescription(),
                film.getDurationInMinutes(),
                film.getGenre() != null ? film.getGenre().getId() : null
        );
    }

    @Override
    public Film update(final Integer id, final Film film) {
        return jdbcTemplate.queryForObject(
                UPDATE_FILM,
                filmRowMapper,
                id,
                film.getTitle(),
                film.getDescription(),
                film.getDurationInMinutes(),
                film.getGenre() != null ? film.getGenre().getId() : null
        );
    }

    @Override
    public void delete(final Integer id) {
        jdbcTemplate.update(DELETE_FILM_BY_ID, id);
    }

    private static final String GET_ALL_FILMS = "SELECT * FROM film.get_all_films()";
    private static final String GET_FILMS_BY_GENRE_NAME = "SELECT * FROM film.get_films_by_genre_name(?)";
    private static final String GET_FILM_BY_ID = "SELECT * FROM film.get_film_by_id(?)";
    private static final String EXISTS_BY_ID = "SELECT * FROM film.check_film_exists_by_id(?)";
    private static final String EXISTS_BY_TITLE = "SELECT * FROM film.check_film_exists_by_title(?)";
    private static final String SAVE_FILM = "SELECT * FROM film.save_film(?, ?, ?, ?)";
    private static final String UPDATE_FILM = "SELECT * FROM film.update_film(?, ?, ?, ?, ?)";
    private static final String DELETE_FILM_BY_ID = "CALL film.delete_film_by_id(?)";

}
