package cinema.management.app.authservice.service;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.request.UpdateUserRoleRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import org.springframework.data.relational.core.sql.Update;

import java.util.List;

public interface ManagerService {

    List<UserResponseDto> getAllUsers();

    void setRoleToUserById(final Integer id, final UpdateUserRoleRequestDto dto);

}
