package cinema.management.app.authservice.mapper;

import cinema.management.app.authservice.dto.request.UserSignUpRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    @Mapping(source = "role.name", target = "role")
    UserResponseDto entityToDto(User user);

    User dtoToEntity(UserSignUpRequestDto dto);

}
