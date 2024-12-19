package cinema.management.app.webclient.client.screening.impl;

import cinema.management.app.webclient.client.screening.ScreeningRestClient;
import cinema.management.app.webclient.dto.screening.request.ScreeningCreationRequestDto;
import cinema.management.app.webclient.dto.screening.request.SeatReserveRequestDto;
import cinema.management.app.webclient.dto.screening.response.ScreeningResponseDto;
import cinema.management.app.webclient.dto.screening.response.SeatResponseDto;
import cinema.management.app.webclient.exception.*;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
public class ScreeningRestClientImpl implements ScreeningRestClient {

    private static final ParameterizedTypeReference<List<ScreeningResponseDto>> SCREENINGS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};

    private static final ParameterizedTypeReference<List<SeatResponseDto>> SEATS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};

    private final RestClient screeningRestClient;

    @Override
    public List<ScreeningResponseDto> findAllScreenings() {
        try {
            return screeningRestClient
                    .get()
                    .uri(SCREENING_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(SCREENINGS_TYPE_REFERENCE);
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        }
    }

    @Override
    public ScreeningResponseDto findScreeningById(Integer id) {
        try {
            return screeningRestClient
                    .get()
                    .uri(SCREENING_BASE_URI + "/" + id)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(ScreeningResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new ScreeningNotFoundException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        }
    }

    @Override
    public void createScreening(
            final LocalDate date,
            final LocalTime time,
            final Integer filmId,
            final Integer hallId
    ) {
        try {
            screeningRestClient
                    .post()
                    .uri(SCREENING_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new ScreeningCreationRequestDto(
                            date,
                            time,
                            filmId,
                            hallId
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.UnprocessableEntity exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new InvalidScreeningTimeFrameException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.Forbidden exception) {
            throw new AccessDeniedException(List.of(
                    "Недостаточно прав доступа"
            ));
        }
    }

    @Override
    public void reserveSeat(final Integer screeningId) {
        try {
            Integer rowNumber;
            Integer seatInRow;
            List<SeatResponseDto> availableSeats = findAvailableSeatsOnScreening(screeningId);

            if (availableSeats.isEmpty()) {
                throw new NoSeatsAvailableException(List.of(
                        "Нет свободных мест"
                ));
            } else {
                rowNumber = availableSeats.getFirst().rowNumber();
                seatInRow = availableSeats.getFirst().seatInRow();
            }

            screeningRestClient
                    .patch()
                    .uri(SCREENING_BASE_URI + "/" + screeningId + "/seats")
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new SeatReserveRequestDto(
                            rowNumber,
                            seatInRow
                    ))
                    .retrieve()
                    .body(SeatResponseDto.class);
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new ScreeningNotFoundException(
                    (List<String>) problemDetail.getProperties().get("errors")
            );
        } catch (HttpClientErrorException.UnprocessableEntity exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new ScreeningPassedException(
                    (List<String>) problemDetail.getProperties().get("errors")
            );
        } catch (HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new SeatAlreadyBookedException(
                    (List<String>) problemDetail.getProperties().get("errors")
            );
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        }
    }

    @Override
    public void deleteAllPassedScreenings() {
        try {
            screeningRestClient
                    .delete()
                    .uri(SCREENING_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.Forbidden exception) {
            throw new AccessDeniedException(List.of(
                    "Недостаточно прав доступа"
            ));
        }
    }

    private List<SeatResponseDto> findAvailableSeatsOnScreening(final Integer screeningId) {
            return screeningRestClient
                    .get()
                    .uri(SCREENING_BASE_URI + "/" + screeningId + "/seats")
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(SEATS_TYPE_REFERENCE);
    }
}


