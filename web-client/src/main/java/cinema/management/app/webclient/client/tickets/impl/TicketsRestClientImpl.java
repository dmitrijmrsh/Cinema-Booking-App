package cinema.management.app.webclient.client.tickets.impl;

import cinema.management.app.webclient.client.tickets.TicketsRestClient;
import cinema.management.app.webclient.dto.auth.response.UserResponseDto;
import cinema.management.app.webclient.dto.tickets.response.TicketResponseDto;
import cinema.management.app.webclient.exception.UserNotFoundException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class TicketsRestClientImpl implements TicketsRestClient {

    private static final ParameterizedTypeReference<List<TicketResponseDto>> TICKETS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};

    private final RestClient ticketsRestClient;

    @Override
    public List<TicketResponseDto> findAllTicketsForCurrentUser() {
        try {
            final Integer currentUserId = getCurrentUserInfo().id();

            return ticketsRestClient
                    .get()
                    .uri(TICKETS_BASE_URI + "/by-user/" + currentUserId)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(TICKETS_TYPE_REFERENCE);
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new UserNotFoundException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        }
    }

    @Override
    public void deleteAllExpiredTicketsForCurrentUser() {
        try {
            final Integer currentUserId = getCurrentUserInfo().id();

            ticketsRestClient
                    .delete()
                    .uri(TICKETS_BASE_URI + "/by-user/" + currentUserId)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new UserNotFoundException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        }
    }

    private UserResponseDto getCurrentUserInfo() {
            return ticketsRestClient
                    .get()
                    .uri(USER_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(UserResponseDto.class);
    }
}
