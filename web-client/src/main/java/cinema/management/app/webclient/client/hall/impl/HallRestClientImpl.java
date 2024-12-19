package cinema.management.app.webclient.client.hall.impl;

import cinema.management.app.webclient.client.hall.HallRestClient;
import cinema.management.app.webclient.dto.hall.request.HallCreateRequestDto;
import cinema.management.app.webclient.dto.hall.response.HallResponseDto;
import cinema.management.app.webclient.exception.AccessDeniedException;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class HallRestClientImpl implements HallRestClient {

    private static final ParameterizedTypeReference<List<HallResponseDto>> HALLS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};

    private final RestClient hallsRestClient;

    @Override
    public List<HallResponseDto> findAllHalls() {
        try {
            return hallsRestClient
                    .get()
                    .uri(HALL_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(HALLS_TYPE_REFERENCE);
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

    @Override
    public void createHall(
            final Integer rowCount,
            final Integer seatInRowCount
    ) {
        try {
            hallsRestClient
                    .post()
                    .uri(HALL_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new HallCreateRequestDto(
                            true,
                            rowCount,
                            seatInRowCount
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.Forbidden exception) {
            throw new AccessDeniedException(List.of(
                    "Недостаточно прав доступа"
            ));
        }
    }
}
