package cinema.management.app.webclient.client.screening;

import cinema.management.app.webclient.dto.screening.response.ScreeningResponseDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScreeningRestClient {

    String SCREENING_BASE_URI = "http://localhost:8222/api/v1/screenings";

    List<ScreeningResponseDto> findAllScreenings();

    ScreeningResponseDto findScreeningById(final Integer id);

    void createScreening(
            final LocalDate date,
            final LocalTime time,
            final Integer filmId,
            final Integer hallId
    );

    void reserveSeat(final Integer screeningId);

    void deleteAllPassedScreenings();

}
