package cinema.management.app.screeningservice.service;

import cinema.management.app.screeningservice.dto.request.SeatReserveRequestDto;
import cinema.management.app.screeningservice.dto.response.SeatResponseDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface SeatService {

    List<SeatResponseDto> findAvailableSeatsOnScreening(final Integer screeningId);

    SeatResponseDto findById(final Integer id);

    SeatResponseDto reserveSeat(
            final Integer screeningId,
            final SeatReserveRequestDto dto,
            HttpServletRequest httpRequest
    );

}
