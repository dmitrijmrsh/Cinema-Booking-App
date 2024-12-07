package cinema.management.app.apigateway.security;

import cinema.management.app.apigateway.exception.CustomException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.hc.core5.http.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.undertow.ConfigurableUndertowWebServerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Locale;

@Service
public class JwtTokenProvider {

    private final MessageSource messageSource;
    private final SecretKey jwtAccessSecretKey;

    public JwtTokenProvider(
            @Value("${security.jwt.token.secret.access}") String jwtAccessSecret,
            MessageSource messageSource
    ) {
        this.jwtAccessSecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret));
        this.messageSource = messageSource;
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

        } catch (ExpiredJwtException expiredJwtException) {

            throw new CustomException(
                    this.messageSource.getMessage(
                        "jwt.validation.errors.expired",
                        new Object[0],
                        LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        } catch (UnsupportedJwtException unsupportedJwtException) {

            throw new CustomException(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.unsupported",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        } catch (MalformedJwtException malformedJwtException) {

            throw new CustomException(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.malformed",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        } catch (Exception exception) {

            throw new CustomException(
                    this.messageSource.getMessage(
                            "jwt.validation.errors.casual.error",
                            new Object[0],
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );

        }
    }
}
