package cinema.management.app.filmservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestBeans {

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgreSQLContainerBean() {
        return new PostgreSQLContainer<>("postgres:16");
    }
}
