package cinema.management.app.authservice.service.impl;

import cinema.management.app.authservice.dto.request.UpdateUserRoleRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.entity.User;
import cinema.management.app.authservice.exception.CustomException;
import cinema.management.app.authservice.mapper.UserMapper;
import cinema.management.app.authservice.repository.RoleRepository;
import cinema.management.app.authservice.repository.UserRepository;
import cinema.management.app.authservice.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::entityToDto)
                .toList();
    }

    @Override
    public void setRoleToUserById(Integer id, UpdateUserRoleRequestDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "user.auth.errors.user.not.found.by.id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));

        user.setRole(
                roleRepository.findByName(dto.role())
                        .orElseThrow(() -> new CustomException(
                                this.messageSource.getMessage(
                                        "user.auth.errors.role.not.found.by.name",
                                        new Object[]{dto.role()},
                                        LocaleContextHolder.getLocale()
                                ),
                                HttpStatus.NOT_FOUND
                        ))
        );

        userRepository.update(id, user);
    }
}
