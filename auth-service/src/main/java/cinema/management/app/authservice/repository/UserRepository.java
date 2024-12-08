package cinema.management.app.authservice.repository;

import cinema.management.app.authservice.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(final Integer id);

    Optional<User> findByEmail(final String email);

    Boolean existsById(final Integer id);

    Boolean existsByEmail(final String email);

    User save(final User user);

    User update(final Integer id, final User user);

    void deleteById(final Integer id);

}
