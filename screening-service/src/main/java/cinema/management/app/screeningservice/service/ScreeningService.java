package cinema.management.app.screeningservice.service;

import cinema.management.app.screeningservice.dto.request.ScreeningCreationRequestDto;
import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningService {

    List<ScreeningResponseDto> findAllScreenings();

    List<ScreeningResponseDto> findScreeningsByDate(LocalDate date);

    List<ScreeningResponseDto> findScreeningsByFilmId(Integer id);

    ScreeningResponseDto findScreeningById(Integer id);

    ScreeningResponseDto saveScreening(ScreeningCreationRequestDto dto);

    void deleteScreening(Integer id);

}
