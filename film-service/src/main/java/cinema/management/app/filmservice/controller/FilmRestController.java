package cinema.management.app.filmservice.controller;

import cinema.management.app.filmservice.dto.CreatedFilmDto;
import cinema.management.app.filmservice.dto.FilmRequestDto;
import cinema.management.app.filmservice.dto.FilmResponseDto;
import cinema.management.app.filmservice.entity.enums.FilmCategory;
import cinema.management.app.filmservice.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/films")
public class FilmRestController {

    private final FilmService filmService;

    @PostMapping
    public ResponseEntity<?> saveFilm(
            @Valid @RequestBody FilmRequestDto filmRequestDto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        CreatedFilmDto createdFilmDto = filmService.save(filmRequestDto);

        return ResponseEntity.ok()
                .body(createdFilmDto);
    }

    @GetMapping
    public ResponseEntity<?> findAllFilms() {
        List<FilmResponseDto> allFilms = filmService.findAll();
        return ResponseEntity.ok()
                .body(allFilms);
    }

    @GetMapping("/{id:\\d+}")
    public FilmResponseDto findFilmById(@PathVariable("id") Long id) {
        return filmService.findById(id);
    }

    @GetMapping("category")
    public ResponseEntity<?> getFilmsByCategory(@RequestParam("category")FilmCategory filmCategory) {
        List<FilmResponseDto> films = filmService.findByCategory(filmCategory);
        return ResponseEntity.ok()
                .body(films);
    }

    @DeleteMapping("{id:\\d+}")
    public ResponseEntity<?> deleteFilm(@PathVariable("id") Long id) {
        filmService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }

}
