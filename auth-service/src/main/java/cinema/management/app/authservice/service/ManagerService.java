package cinema.management.app.authservice.service;

import cinema.management.app.authservice.dto.request.UpdateUserRoleRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;

import java.util.List;

public interface ManagerService {

    List<UserResponseDto> getAllUsers();

    void setRoleToUserById(final Integer id, final UpdateUserRoleRequestDto dto);

    void deleteUserById(final Integer id);

}
