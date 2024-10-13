package cinema.management.app.screeningservice.controller;

import cinema.management.app.screeningservice.dto.request.SeatReserveRequestDto;
import cinema.management.app.screeningservice.dto.response.SeatResponseDto;
import cinema.management.app.screeningservice.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/screenings/{screeningId:\\d+}/seats")
public class SeatRestController {

    private final SeatService seatService;

    @GetMapping
    public List<SeatResponseDto> findAvailableSeatsOnScreening(
            @PathVariable("screeningId") Integer screeningId
    ) {
        return seatService.findAvailableSeatsOnScreening(screeningId);
    }

    @PatchMapping
    public ResponseEntity<?> reserveSeat(
            @PathVariable("screeningId") Integer screeningId,
            @Valid @RequestBody SeatReserveRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        SeatResponseDto responseDto = seatService.reserveSeat(screeningId, dto);

        return ResponseEntity.ok()
                .body(responseDto);
    }
}
