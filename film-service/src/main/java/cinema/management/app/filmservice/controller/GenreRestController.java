package cinema.management.app.filmservice.controller;

import cinema.management.app.filmservice.dto.response.GenreResponseDto;
import cinema.management.app.filmservice.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/films/genres/{genreId:\\d+}")
public class GenreRestController {

    private final GenreService genreService;

    @GetMapping
    public GenreResponseDto getGenre(@PathVariable("genreId") final Integer id) {
        return genreService.findGenreById(id);
    }

}
