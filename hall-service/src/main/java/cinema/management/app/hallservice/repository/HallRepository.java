package cinema.management.app.hallservice.repository;

import cinema.management.app.hallservice.entity.Hall;

import java.util.List;
import java.util.Optional;

public interface HallRepository {

    List<Hall> findAll();

    List<Hall> findHallsByIsActive(final Boolean isActive);

    Optional<Hall> findById(final Integer id);

    Hall save(final Hall hall);

    Hall update(final Integer id, final Hall hall);

    void deleteById(final Integer id);

}
