package cinema.management.app.webclient.client.auth;

import cinema.management.app.webclient.dto.auth.response.AccessTokenResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserLogInResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserResponseDto;

public interface AuthRestClient {

    String AUTH_BASE_URI = "http://localhost:8222/api/v1/auth/";

    UserResponseDto signUp(
            final String firstName,
            final String lastName,
            final String email,
            final String password
    );

    UserLogInResponseDto logIn(
            final String email,
            final String password
    );

    AccessTokenResponseDto getAccessToken(final String refreshToken);

    void logOut();

}
