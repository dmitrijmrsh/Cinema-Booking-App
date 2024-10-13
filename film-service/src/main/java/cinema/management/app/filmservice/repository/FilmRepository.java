package cinema.management.app.filmservice.repository;

import cinema.management.app.filmservice.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Integer> {

    @Query("SELECT f FROM Film f WHERE f.genre.name = :genre")
    List<Film> findByGenreName(@Param("genre") String genre);

    boolean existsByTitle(String title);
}
