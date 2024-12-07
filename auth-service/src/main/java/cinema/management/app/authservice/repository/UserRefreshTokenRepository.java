package cinema.management.app.authservice.repository;

import cinema.management.app.authservice.entity.EmailToRefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRefreshTokenRepository extends CrudRepository<EmailToRefreshToken, String> {
}
