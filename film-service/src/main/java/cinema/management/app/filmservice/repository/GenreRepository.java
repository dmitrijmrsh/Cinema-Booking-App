package cinema.management.app.filmservice.repository;

import cinema.management.app.filmservice.entity.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {

    List<Genre> findAll();

    Optional<Genre> findById(final Integer id);

    Optional<Genre> findByName(final String name);

    Boolean existsByName(final String name);

    Genre save(final Genre genre);

}
