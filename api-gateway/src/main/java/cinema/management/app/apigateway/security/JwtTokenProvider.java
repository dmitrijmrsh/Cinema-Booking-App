package cinema.management.app.apigateway.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;

@Service
public class JwtTokenProvider {

    private final SecretKey jwtAccessSecretKey;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret.access}") String jwtAccessSecret
    ) {
        this.jwtAccessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
    }

    public String getEmailFromAccessToken(String accessToken) {
        return getAccessClaims(accessToken).getSubject();
    }

    public String getPasswordFromAccessToken(String accessToken) {
        return getAccessClaims(accessToken).get("password", String.class);
    }

    public String getRoleFromAccessToken(String accessToken) {
        return getAccessClaims(accessToken).get("role", String.class);
    }

    public boolean validateAccessToken(String accessToken) {
        return validateToken(accessToken, jwtAccessSecretKey);
    }

    private Claims getAccessClaims(@NonNull String accessToken) {
        return getClaims(accessToken, jwtAccessSecretKey);
    }

    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        validateToken(token, secret);
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secretKey) {
        try {

            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;

        } catch (Exception ex) {

            return false;

        }
    }
}
