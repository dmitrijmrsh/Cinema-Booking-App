package cinema.management.app.screeningservice.repository;

import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    Optional<Seat> findByScreeningAndRowsNumberAndSeatInRow(Screening screening, Integer rowsNumber, Integer seatInRow);

}
