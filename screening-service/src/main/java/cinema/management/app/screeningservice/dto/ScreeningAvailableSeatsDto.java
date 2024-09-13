package cinema.management.app.screeningservice.dto;

import cinema.management.app.screeningservice.entity.Seat;

import java.util.List;

public record ScreeningAvailableSeatsDto(
        List<Seat> availableSeats
) {
}
