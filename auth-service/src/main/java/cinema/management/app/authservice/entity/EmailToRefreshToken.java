package cinema.management.app.authservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash("email_to_refresh_token")
public class EmailToRefreshToken {

    @Id
    private String email;

    private String refreshToken;

}
