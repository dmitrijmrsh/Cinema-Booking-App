package cinema.management.app.authservice.repository;

import cinema.management.app.authservice.entity.Role;

import java.util.Optional;

public interface RoleRepository {

    Optional<Role> findByName(final String name);

    Boolean existsByName(final String name);

}
