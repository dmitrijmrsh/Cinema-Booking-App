package cinema.management.app.authservice.service.impl;

import cinema.management.app.authservice.dto.request.RefreshTokenRequestDto;
import cinema.management.app.authservice.dto.request.UserLogInRequestDto;
import cinema.management.app.authservice.dto.request.UserSignUpRequestDto;
import cinema.management.app.authservice.dto.response.AccessTokenResponseDto;
import cinema.management.app.authservice.dto.response.UserLogInResponseDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.service.AuthService;

public class AuthServiceImpl implements AuthService {

    @Override
    public UserResponseDto signUp(UserSignUpRequestDto dto) {
        return null;
    }

    @Override
    public UserLogInResponseDto login(UserLogInRequestDto dto) {
        return null;
    }

    @Override
    public AccessTokenResponseDto getAccessToken(RefreshTokenRequestDto dto) {
        return null;
    }
}
