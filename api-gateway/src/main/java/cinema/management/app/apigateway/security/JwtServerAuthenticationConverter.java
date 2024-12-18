package cinema.management.app.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtServerAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BEARER = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String token;
        HttpCookie tokenCookie = exchange.getRequest().getCookies().getFirst("AuthToken");

        if (tokenCookie != null && !tokenCookie.getValue().isEmpty()) {
            token = tokenCookie.getValue();

            if (!jwtTokenProvider.validateAccessToken(token)) {
                exchange.getResponse().addCookie(ResponseCookie.from("AuthToken", "")
                        .httpOnly(true)
                        .secure(true)
                        .path("/")
                        .maxAge(3600)
                        .build()
                );
                token = null;
            }
        } else {
            token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        }

        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(BEARER.length());
        }

        UserDetails userDetails = createUserDetails(token);

        if (token == null || userDetails == null) {
            return Mono.empty();
        }

        return Mono.just(token).map(t -> JwtToken.of(t, userDetails));
    }

    private UserDetails createUserDetails(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }

        String email = jwtTokenProvider.getEmailFromAccessToken(token);
        String password = jwtTokenProvider.getPasswordFromAccessToken(token);
        String role = jwtTokenProvider.getRoleFromAccessToken(token);

        return org.springframework.security.core.userdetails.User.builder()
                .username(email)
                .password(password)
                .authorities(
                        role.startsWith("ROLE_")
                                ? role
                                : "ROLE_%s".formatted(role)
                )
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
