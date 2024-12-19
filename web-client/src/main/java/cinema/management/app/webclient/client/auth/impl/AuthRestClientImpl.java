package cinema.management.app.webclient.client.auth.impl;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.dto.auth.request.*;
import cinema.management.app.webclient.dto.auth.response.AccessTokenResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserLogInResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserResponseDto;
import cinema.management.app.webclient.exception.*;
import cinema.management.app.webclient.util.SecurityUtil;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class AuthRestClientImpl implements AuthRestClient {

    private static final ParameterizedTypeReference<List<UserResponseDto>> USERS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {};

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
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        }
    }

    @Override
    public void checkAccessSpecialFeatures() {
        try {
            final String currentUserRole = getCurrentUser().role();

            if (currentUserRole.equals(ADMIN) || currentUserRole.equals(MANAGER)) {
                return;
            }

            throw new AccessDeniedException(List.of(
                    "Пользователь имеет недостаточно прав доступа"
            ));
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
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
    public UserResponseDto getCurrentUserInfo() {
        try {
            return getCurrentUser();
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new UserNotFoundException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        try {
            return authRestClient
                    .get()
                    .uri(MANAGER_BASE_URI + "/all-users")
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .retrieve()
                    .body(USERS_TYPE_REFERENCE);
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
    public void setUserRoleToManager(Integer userId) {
        try {
            authRestClient
                    .patch()
                    .uri(ADMIN_BASE_URI + "/" + userId)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .body(new UpdateUserRoleRequestDto(
                            "MANAGER"
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.Forbidden exception) {
            throw new AccessDeniedException(List.of(
                    "Недостаточно прав пользователя"
            ));
        }
    }

    @Override
    public void updateCurrentUser(
            final String firstName,
            final String lastName,
            final String email
    ) {
        try {
            authRestClient
                    .patch()
                    .uri(USER_BASE_URI)
                    .header(
                            HttpHeaders.COOKIE,
                            "AuthToken=" + SecurityUtil.getAccessToken()
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateUserRequestDto(
                            firstName,
                            lastName,
                            email
                    ))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.Unauthorized exception) {
            throw new UserUnauthorizedException(List.of(
                    "Пользователь не авторизован"
            ));
        } catch (HttpClientErrorException.NotFound exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new UserNotFoundException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void logOut() {
        authRestClient
                .delete()
                .uri(AUTH_BASE_URI + "logout")
                .header(
                        HttpHeaders.COOKIE,
                        "AuthToken=" + SecurityUtil.getAccessToken()
                )
                .retrieve()
                .toBodilessEntity();
    }

    private UserResponseDto getCurrentUser() {
        return authRestClient
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
