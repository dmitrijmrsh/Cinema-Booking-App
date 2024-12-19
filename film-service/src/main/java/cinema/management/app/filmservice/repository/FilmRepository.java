package cinema.management.app.filmservice.repository;

import cinema.management.app.filmservice.entity.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {

    List<Film> findAll();

    List<Film> findAllByGenreName(final String genreName);

    Optional<Film> findById(final Integer id);

    Boolean existsById(final Integer id);

    Boolean existsByTitle(final String name);

    Film save(final Film film);

    Film update(final Integer id, final Film film);

    void delete(final Integer id);

}
