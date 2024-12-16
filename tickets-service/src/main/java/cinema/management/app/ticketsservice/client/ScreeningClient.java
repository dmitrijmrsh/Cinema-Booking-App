package cinema.management.app.ticketsservice.client;

import cinema.management.app.ticketsservice.dto.response.ScreeningDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "screening-service", url = "${application.config.screening-url}")
public interface ScreeningClient {

    @GetMapping("{screeningId:\\d+}")
    ScreeningDto findScreeningById(
            @PathVariable("screeningId") final Integer screeningId
    );

}
