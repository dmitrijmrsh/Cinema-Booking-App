package cinema.management.app.screeningservice.controller;

import cinema.management.app.screeningservice.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/screenings/seats/{seatId:\\d+}")
@RequiredArgsConstructor
public class SeatRestController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<?> findSeatById(
            @PathVariable("seatId") final Integer seatId
    ) {
        return ResponseEntity.ok()
                .body(seatService.findById(seatId));
    }

}
