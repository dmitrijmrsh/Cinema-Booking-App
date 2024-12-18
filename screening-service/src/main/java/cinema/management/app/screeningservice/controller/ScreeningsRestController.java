package cinema.management.app.screeningservice.controller;

import cinema.management.app.screeningservice.dto.request.ScreeningCreationRequestDto;
import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;
import cinema.management.app.screeningservice.service.ScreeningService;
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
@RequestMapping("/api/v1/screenings")
public class ScreeningsRestController {

    private final ScreeningService screeningService;

    @GetMapping
    public List<ScreeningResponseDto> findAllScreenings() {
        return screeningService.findAllScreenings();
    }

    @PostMapping
    public ResponseEntity<?> createScreening(
            @Valid @RequestBody ScreeningCreationRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        ScreeningResponseDto responseDto = screeningService.saveScreening(dto);
        String entityURI = "/api/v1/screenings/" + responseDto.id();

        return ResponseEntity.created(URI.create(entityURI))
                .body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllPassedScreenings() {
        screeningService.deleteAllPassedScreenings();
        return ResponseEntity.noContent().build();
    }
}
