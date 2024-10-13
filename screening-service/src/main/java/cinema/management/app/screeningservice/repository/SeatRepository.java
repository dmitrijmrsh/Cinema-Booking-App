package cinema.management.app.screeningservice.repository;

import cinema.management.app.screeningservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    @Query("SELECT s FROM Seat s WHERE s.screening.id = :screening_id AND s.status = 'AVAILABLE'")
    List<Seat> findAvailableSeatsByScreeningId(@Param("screening_id") Integer screeningId);

    Optional<Seat> findByScreeningIdAndRowNumberAndSeatInRow(Integer screeningId, Integer rowNumber, Integer seatInRow);

}
