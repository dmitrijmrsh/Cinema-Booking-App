package cinema.management.app.webclient.config;

import cinema.management.app.webclient.client.auth.impl.AuthRestClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public AuthRestClientImpl authRestClient() {
        return new AuthRestClientImpl(RestClient.create());
    }

}
