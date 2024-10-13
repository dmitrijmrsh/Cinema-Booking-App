package cinema.management.app.screeningservice.repository;

import cinema.management.app.screeningservice.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Integer> {

    @Query("SELECT s FROM Screening s WHERE s.date = :date")
    List<Screening> findScreeningsByDate(@Param("date") LocalDate date);

    List<Screening> findScreeningsByFilmId(Integer id);

    List<Screening> findScreeningsByHallIdAndDate(Integer hallId, LocalDate date);

}
