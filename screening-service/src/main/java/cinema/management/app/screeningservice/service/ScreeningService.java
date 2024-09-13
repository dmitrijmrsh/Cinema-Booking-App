package cinema.management.app.screeningservice.service;

import cinema.management.app.screeningservice.dto.ScreeningAvailableSeatsDto;
import cinema.management.app.screeningservice.dto.ScreeningRequestDto;
import cinema.management.app.screeningservice.dto.ScreeningResponseDto;
import cinema.management.app.screeningservice.dto.SeatReservingRequestDto;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningService {

    ScreeningResponseDto saveScreening(ScreeningRequestDto screeningRequestDto, Long filmId);

    ScreeningResponseDto findScreeningById(Long screeningId);

    List<ScreeningResponseDto> findScreeningsByDate(LocalDate date);

    ScreeningAvailableSeatsDto findAvailableSeats(Long screeningId);

    void reserveSeat(SeatReservingRequestDto seatReservingRequestDto);
}
