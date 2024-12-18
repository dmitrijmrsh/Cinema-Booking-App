package cinema.management.app.screeningservice.repository;

import cinema.management.app.screeningservice.entity.Screening;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScreeningRepository {

    List<Screening> findAll();

    List<Screening> findAllByDate(final Date date);

    List<Screening> findAllByFilmId(final Integer filmId);

    List<Screening> findAllByHallIdAndDate(
            final Integer hallId,
            final LocalDate date
    );

    Optional<Screening> findById(final Integer id);

    Screening save(final Screening screening);

    void deleteById(final Integer id);

    void deletePassed();

}
