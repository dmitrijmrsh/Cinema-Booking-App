package cinema.management.app.filmservice.controller;

import cinema.management.app.filmservice.dto.request.FilmCreationRequestDto;
import cinema.management.app.filmservice.dto.response.FilmResponseDto;
import cinema.management.app.filmservice.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/films")
public class FilmsRestController {

    private final FilmService filmService;

    @GetMapping
    public List<FilmResponseDto> findAllFilms() {
        return filmService.findAllFilms();
    }

    @GetMapping("/by_genre")
    public List<FilmResponseDto> findFilmsByCategory(
            @RequestParam("genre") String genre
    ) {
        return filmService.findFilmByGenre(genre);
    }

    @PostMapping
    public ResponseEntity<?> createFilm(
            @Valid @RequestBody FilmCreationRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        FilmResponseDto responseDto = filmService.saveFilm(dto);
        String entityURI = "/api/v1/films/" + responseDto.id();

        return ResponseEntity.created(URI.create(entityURI))
                .body(responseDto);
    }

}
