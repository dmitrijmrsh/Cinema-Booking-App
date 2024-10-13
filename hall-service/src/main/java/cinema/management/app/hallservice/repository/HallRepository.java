package cinema.management.app.hallservice.repository;

import cinema.management.app.hallservice.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Integer> {

    List<Hall> findHallsByIsActive(Boolean isActive);

}
