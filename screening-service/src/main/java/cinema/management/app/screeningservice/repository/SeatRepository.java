package cinema.management.app.screeningservice.repository;

import cinema.management.app.screeningservice.constant.SeatStatus;
import cinema.management.app.screeningservice.entity.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {

    List<Seat> findAvailableByScreeningId(final Integer screeningId);

    Optional<Seat> findByScreeningIdAndRowNumberAndSeatInRow(
            final Integer screeningId,
            final Integer rowNumber,
            final Integer seatInRow
    );

    Seat save(final Seat seat);

    Seat updateStatus(final Integer id, final SeatStatus status);

    void deleteById(final Integer id);

}
