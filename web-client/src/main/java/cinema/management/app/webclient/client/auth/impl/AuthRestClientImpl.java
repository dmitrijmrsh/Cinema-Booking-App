package cinema.management.app.webclient.client.auth.impl;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.dto.auth.request.RefreshTokenRequestDto;
import cinema.management.app.webclient.dto.auth.request.UserLogInRequestDto;
import cinema.management.app.webclient.dto.auth.request.UserSignUpRequestDto;
import cinema.management.app.webclient.dto.auth.response.AccessTokenResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserLogInResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserResponseDto;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.UserAlreadyExistException;
import cinema.management.app.webclient.exception.UserNotFoundException;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AuthRestClientImpl implements AuthRestClient {

    private final RestClient authRestClient;

    @Override
    public UserResponseDto signUp(
            final String firstName,
            final String lastName,
            final String email,
            final String password
    ) {
        try {
            return authRestClient
                    .post()
                    .uri(AUTH_BASE_URI + "signup")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UserSignUpRequestDto(
                            firstName,
                            lastName,
                            email,
                            password
                    ))
                    .retrieve()
                    .body(UserResponseDto.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.UnprocessableEntity exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new UserAlreadyExistException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public UserLogInResponseDto logIn(
            final String email,
            final String password
    ) {
        try {
            return authRestClient
                    .post()
                    .uri(AUTH_BASE_URI + "login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UserLogInRequestDto(
                            email,
                            password
                    ))
                    .retrieve()
                    .body(UserLogInResponseDto.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new UserNotFoundException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public AccessTokenResponseDto getAccessToken(final String refreshToken) {
        try {
            return authRestClient
                    .post()
                    .uri(AUTH_BASE_URI + "token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new RefreshTokenRequestDto(
                            refreshToken
                    ))
                    .retrieve()
                    .body(AccessTokenResponseDto.class);

        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void logOut() {
        authRestClient
                .delete()
                .uri(AUTH_BASE_URI + "logout")
                .retrieve();
    }
}
