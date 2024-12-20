package cinema.management.app.authservice.service;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {

    UserResponseDto findCurrentUser(HttpServletRequest request);

    UserResponseDto updateCurrentUser(HttpServletRequest request, final UpdateUserRequestDto dto);

}
