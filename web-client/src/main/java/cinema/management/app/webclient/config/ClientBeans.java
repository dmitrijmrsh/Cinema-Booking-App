package cinema.management.app.webclient.config;

import cinema.management.app.webclient.client.auth.impl.AuthRestClientImpl;
import cinema.management.app.webclient.client.film.impl.FIlmRestClientImpl;
import cinema.management.app.webclient.client.hall.impl.HallRestClientImpl;
import cinema.management.app.webclient.client.screening.impl.ScreeningRestClientImpl;
import cinema.management.app.webclient.client.tickets.impl.TicketsRestClientImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ClientBeans {

    @Bean
    public AuthRestClientImpl authRestClient() {
        return new AuthRestClientImpl(RestClient.create());
    }

    @Bean
    public ScreeningRestClientImpl screeningRestClient() {
        return new ScreeningRestClientImpl(RestClient.create());
    }

    @Bean
    public TicketsRestClientImpl ticketsRestClient() {
        return  new TicketsRestClientImpl(RestClient.create());
    }

    @Bean
    public FIlmRestClientImpl fIlmRestClient() {
        return new FIlmRestClientImpl(RestClient.create());
    }

    @Bean
    public HallRestClientImpl hallRestClient() {
        return new HallRestClientImpl(RestClient.create());
    }

}
