package cinema.management.app.apigateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoderBean() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity httpSecurity,
            ReactiveAuthenticationManager authenticationManager,
            ServerAuthenticationConverter authenticationConverter
    ) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        authenticationWebFilter.setServerAuthenticationConverter(authenticationConverter);

        return httpSecurity
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/v1/auth/**").permitAll()
                        .pathMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .pathMatchers("/api/v1/manager/**").hasAnyRole( "ADMIN", "MANAGER")
                        .pathMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .pathMatchers("/api/v1/films/**").hasAnyRole("ADMIN", "MANAGER")
                        .pathMatchers("/api/v1//halls/**").hasAnyRole("ADMIN", "MANAGER")
                        .pathMatchers(HttpMethod.POST,"/api/v1/screenings/**").hasAnyRole("ADMIN", "MANAGER")
                        .pathMatchers(HttpMethod.DELETE, "/api/v1/screenings/**").hasAnyRole("ADMIN", "MANAGER")
                        .pathMatchers(HttpMethod.GET,"/api/v1/screenings/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .pathMatchers(HttpMethod.PATCH, "/api/v1/screenings/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .pathMatchers("/api/v1/tickets/**").hasAnyRole("ADMIN", "MANAGER", "USER")
                        .anyExchange().authenticated()
                )
                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(ServerHttpSecurity.CorsSpec::disable)
                .build();
    }
}
