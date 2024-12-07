package cinema.management.app.filmservice.controller;

import cinema.management.app.filmservice.dto.request.GenreCreationRequestDto;
import cinema.management.app.filmservice.dto.response.GenreResponseDto;
import cinema.management.app.filmservice.service.GenreService;
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
@RequestMapping("/api/v1/films/genres")
public class GenresRestController {

    private final GenreService genreService;

    @GetMapping
    public List<GenreResponseDto> getAllGenres() {
        return genreService.findAllGenres();
    }

    @PostMapping
    public ResponseEntity<?> createGenre(
            @Valid @RequestBody final GenreCreationRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        GenreResponseDto responseDto = genreService.saveGenre(dto);
        String entityURI = "/api/v1/films/genres/" + responseDto.id();

        return ResponseEntity.created(URI.create(entityURI))
                .body(responseDto);
    }

}
