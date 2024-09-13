package cinema.management.app.filmservice.repository;

import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.entity.enums.FilmCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f WHERE f.category = :category")
    List<Film> findByCategory(@Param("category")FilmCategory category);

    boolean existsByTitle(String title);
}
