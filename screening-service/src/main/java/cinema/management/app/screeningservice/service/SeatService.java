package cinema.management.app.screeningservice.service;

import cinema.management.app.screeningservice.dto.request.SeatReserveRequestDto;
import cinema.management.app.screeningservice.dto.response.SeatResponseDto;

import java.util.List;

public interface SeatService {

    List<SeatResponseDto> findAvailableSeatsOnScreening(Integer screeningId);

    SeatResponseDto reserveSeat(Integer screeningId, SeatReserveRequestDto dto);

}
