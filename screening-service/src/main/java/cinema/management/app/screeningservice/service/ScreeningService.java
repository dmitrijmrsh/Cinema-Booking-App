package cinema.management.app.screeningservice.service;

import cinema.management.app.screeningservice.dto.request.ScreeningCreationRequestDto;
import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface ScreeningService {

    List<ScreeningResponseDto> findAllScreenings();

    List<ScreeningResponseDto> findScreeningsByDate(final LocalDate date);

    List<ScreeningResponseDto> findScreeningsByFilmId(final Integer filmId);

    ScreeningResponseDto findScreeningById(final Integer id);

    ScreeningResponseDto saveScreening(final ScreeningCreationRequestDto dto);

    void deleteScreening(final Integer id);

    void deleteAllPassedScreenings();

}
