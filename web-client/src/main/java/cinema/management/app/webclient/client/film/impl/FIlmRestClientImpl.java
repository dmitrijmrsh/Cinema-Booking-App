package cinema.management.app.webclient.client.film.impl;

import cinema.management.app.webclient.client.film.FilmRestClient;
import cinema.management.app.webclient.dto.film.request.FilmCreationRequestDto;
import cinema.management.app.webclient.dto.film.response.FilmResponseDto;
import cinema.management.app.webclient.exception.AccessDeniedException;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.FilmAlreadyExistException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.List;

@RequiredArgsConstructor
public class FIlmRestClientImpl implements FilmRestClient {

    private static final ParameterizedTypeReference<List<FilmResponseDto>> FILMS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};

    private final RestClient filmsRestClient;

    @Override
    public List<FilmResponseDto> findAllFilms() {
        try {
            return filmsRestClient
                    .get()
                    .uri(FILM_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(FILMS_TYPE_REFERENCE);
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.Forbidden exception) {
            throw new AccessDeniedException(List.of(
                    "Пользователь имеет недостаточно прав доступа"
            ));
        }
    }

    @Override
    public void createFilm(
            final String title,
            final String genre,
            final String description,
            final Integer durationInMinutes
    ) {
        try {
            filmsRestClient
                    .post()
                    .uri(FILM_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new FilmCreationRequestDto(
                            title,
                            genre,
                            description,
                            durationInMinutes
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.Conflict exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new FilmAlreadyExistException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }  catch (HttpClientErrorException.Forbidden exception) {
            throw new AccessDeniedException(List.of(
                    "Пользователь имеет недостаточно прав доступа"
            ));
        }
    }
}
