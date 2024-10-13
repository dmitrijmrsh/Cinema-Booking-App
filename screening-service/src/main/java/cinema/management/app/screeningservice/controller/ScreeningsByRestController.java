package cinema.management.app.screeningservice.controller;

import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;
import cinema.management.app.screeningservice.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/screenings/by")
public class ScreeningsByRestController {

    private final ScreeningService screeningService;

    @GetMapping("date")
    List<ScreeningResponseDto> findScreeningsByDate(@RequestParam("date") LocalDate date) {
        return screeningService.findScreeningsByDate(date);
    }

    @GetMapping("film/{filmId:\\d+}")
    List<ScreeningResponseDto> findScreeningsByFilmId(@PathVariable("filmId") Integer filmId) {
        return screeningService.findScreeningsByFilmId(filmId);
    }

}
