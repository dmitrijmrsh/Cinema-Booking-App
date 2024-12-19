package cinema.management.app.webclient.client.auth;

import cinema.management.app.webclient.dto.auth.response.AccessTokenResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserLogInResponseDto;
import cinema.management.app.webclient.dto.auth.response.UserResponseDto;

import java.util.List;

public interface AuthRestClient {

    String AUTH_BASE_URI = "http://localhost:8222/api/v1/auth/";
    String USER_BASE_URI = "http://localhost:8222/api/v1/user";
    String MANAGER_BASE_URI = "http://localhost:8222/api/v1/manager";
    String ADMIN_BASE_URI = "http://localhost:8222/api/v1/admin";

    String ADMIN = "ADMIN";
    String MANAGER = "MANAGER";

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

    void checkAccessSpecialFeatures();

    AccessTokenResponseDto getAccessToken(final String refreshToken);

    UserResponseDto getCurrentUserInfo();

    List<UserResponseDto> getAllUsers();

    void setUserRoleToManager(final Integer userId);

    void updateCurrentUser(
            final String firstName,
            final String lastName,
            final String email
    );

    void logOut();

}
