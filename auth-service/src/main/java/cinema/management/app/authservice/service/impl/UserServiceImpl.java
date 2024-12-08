package cinema.management.app.authservice.service.impl;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.entity.EmailToRefreshToken;
import cinema.management.app.authservice.entity.User;
import cinema.management.app.authservice.exception.CustomException;
import cinema.management.app.authservice.mapper.UserMapper;
import cinema.management.app.authservice.repository.UserRefreshTokenRepository;
import cinema.management.app.authservice.repository.UserRepository;
import cinema.management.app.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRefreshTokenRepository refreshStorage;
    private final MessageSource messageSource;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto findUserById(Integer id) {
        return userMapper.entityToDto(
                findById(id)
        );
    }

    @Override
    public UserResponseDto updateUserById(Integer id, UpdateUserRequestDto dto) {
        User user = findById(id);

        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        return userMapper.entityToDto(
                userRepository.update(id, user)
        );
    }

    @Override
    public void deleteUserById(Integer id) {
        final String email = findById(id).getEmail();
        userRepository.deleteById(id);
        Optional<EmailToRefreshToken> mayBeEmailToRefreshToken = refreshStorage.findById(email);
        mayBeEmailToRefreshToken.ifPresent(refreshStorage::delete);
    }

    private User findById(final Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "user.auth.errors.user.not.found.by.id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }
}
