package cinema.management.app.screeningservice.client;

import cinema.management.app.filmservice.entity.Film;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "film-service", url = "${application.config.films-url}")
public interface FilmClient {

    @GetMapping("{id:\\d+}")
    Film findFilmById(@PathVariable("id") Long id);

}
