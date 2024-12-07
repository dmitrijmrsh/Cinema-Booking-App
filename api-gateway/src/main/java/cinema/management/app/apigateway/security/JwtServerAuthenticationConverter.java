package cinema.management.app.apigateway.security;

import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.handler.predicate.HeaderRoutePredicateFactory;
import org.springframework.http.HttpCookie;
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
    private final HeaderRoutePredicateFactory headerRoutePredicateFactory;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String token;
        HttpCookie tokenFromCookie = exchange.getRequest().getCookies().getFirst("AuthToken");

        if (tokenFromCookie == null) {
            token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        } else {
            token = tokenFromCookie.toString();
        }

        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(BEARER.length());
        }

        return Mono.justOrEmpty(token).map(t -> JwtToken.of(t, createUserDetails(t)));
    }

    private UserDetails createUserDetails(String token) {
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
