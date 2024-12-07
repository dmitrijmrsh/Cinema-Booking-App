package cinema.management.app.authservice.service;

import cinema.management.app.authservice.dto.request.RefreshTokenRequestDto;
import cinema.management.app.authservice.dto.request.UserLogInRequestDto;
import cinema.management.app.authservice.dto.request.UserSignUpRequestDto;
import cinema.management.app.authservice.dto.response.AccessTokenResponseDto;
import cinema.management.app.authservice.dto.response.UserLogInResponseDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;

public interface AuthService {

    UserResponseDto signUp(UserSignUpRequestDto dto);

    UserLogInResponseDto login(UserLogInRequestDto dto);

    AccessTokenResponseDto getAccessToken(RefreshTokenRequestDto dto);

}
