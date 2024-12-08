package cinema.management.app.authservice.service;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;

public interface UserService {

    UserResponseDto findUserById(final Integer id);

    UserResponseDto updateUserById(final Integer id, final UpdateUserRequestDto dto);

    void deleteUserById(final Integer id);

}
