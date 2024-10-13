package cinema.management.app.screeningservice.client;

import cinema.management.app.screeningservice.dto.response.FilmDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "film-service", url = "${application.config.films-url}")
public interface FilmClient {

    @GetMapping("{id:\\d+}")
    FilmDto findFilmById(@PathVariable("id") Integer id);

}
