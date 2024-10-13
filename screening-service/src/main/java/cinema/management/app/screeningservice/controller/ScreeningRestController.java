package cinema.management.app.screeningservice.controller;

import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;
import cinema.management.app.screeningservice.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/screenings/{screeningId:\\d+}")
@RequiredArgsConstructor
public class ScreeningRestController {

    private final ScreeningService screeningService;

    @GetMapping
    public ScreeningResponseDto findScreeningById(
            @PathVariable("screeningId") Integer screeningId
    ) {
        return screeningService.findScreeningById(screeningId);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteScreening(
            @PathVariable("screeningId") Integer screeningId
    ) {
        screeningService.deleteScreening(screeningId);
        return ResponseEntity.noContent()
                .build();
    }

}
