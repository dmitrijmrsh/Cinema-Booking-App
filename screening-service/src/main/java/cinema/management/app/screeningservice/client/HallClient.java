package cinema.management.app.screeningservice.client;

import cinema.management.app.screeningservice.dto.response.HallDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hall-service", url = "${application.config.halls-url}")
public interface HallClient {

    @GetMapping("{id:\\d+}")
    HallDto findHallById(@PathVariable("id") Integer id);

}
