package cinema.management.app.filmservice.controller;

import cinema.management.app.filmservice.dto.request.FilmUpdateRequestDto;
import cinema.management.app.filmservice.dto.response.FilmResponseDto;
import cinema.management.app.filmservice.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/films/{filmId:\\d+}")
public class FilmRestController {

    private final FilmService filmService;

    @GetMapping
    public FilmResponseDto findFilmById(@PathVariable("filmId") Integer id) {
        return filmService.findFilmById(id);
    }

    @PutMapping
    public ResponseEntity<?> updateFilmById(
            @PathVariable("filmId") Integer id,
            @Valid @RequestBody FilmUpdateRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        FilmResponseDto responseDto = filmService.updateFilm(id, dto);

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFilmById(@PathVariable("filmId") Integer id) {
        filmService.deleteFilm(id);
        return ResponseEntity.noContent()
                .build();
    }
}
