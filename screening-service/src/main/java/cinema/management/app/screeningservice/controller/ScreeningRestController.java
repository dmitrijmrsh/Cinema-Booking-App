package cinema.management.app.screeningservice.controller;

import cinema.management.app.screeningservice.dto.ScreeningRequestDto;
import cinema.management.app.screeningservice.dto.ScreeningResponseDto;
import cinema.management.app.screeningservice.dto.SeatReservingRequestDto;
import cinema.management.app.screeningservice.service.ScreeningService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/screenings")
@RequiredArgsConstructor
public class ScreeningRestController {

    private final ScreeningService screeningService;

    @GetMapping
    public ResponseEntity<?> findScreeningsByDate(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        List<ScreeningResponseDto> screenings = screeningService.findScreeningsByDate(date);
        return ResponseEntity.ok()
                .body(screenings);
    }

    @GetMapping("{screeningId:\\d+}")
    public ResponseEntity<?> findScreeningById(
            @PathVariable("screeningId") Long screeningId
    ) {
        return ResponseEntity.ok()
                .body(screeningService.findScreeningById(screeningId));
    }

    @GetMapping("/seats/{screeningId:\\d+}")
    public ResponseEntity<?> findAvailableSeatsOnScreening(
            @PathVariable("screeningId") Long screeningId
    ) {
        return ResponseEntity.ok()
                .body(screeningService.findAvailableSeats(screeningId));
    }

    @PostMapping("{filmId:\\d+}")
    public ResponseEntity<?> saveScreening(
            @Valid @RequestBody ScreeningRequestDto screeningRequestDto,
            @PathVariable("filmId") Long filmId,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        ScreeningResponseDto screeningResponseDto = screeningService.saveScreening(screeningRequestDto, filmId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(screeningResponseDto);
    }

    @PatchMapping
    public ResponseEntity<?> reserveSeatInScreening(
            @Valid @RequestBody SeatReservingRequestDto seatReservingRequestDto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        screeningService.reserveSeat(seatReservingRequestDto);

        return ResponseEntity.noContent()
                .build();
    }
}
