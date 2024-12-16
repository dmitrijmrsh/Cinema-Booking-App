package cinema.management.app.ticketsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TicketsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketsServiceApplication.class, args);
    }

}
