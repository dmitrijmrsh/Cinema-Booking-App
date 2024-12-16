package cinema.management.app.authservice.service.impl;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.entity.User;
import cinema.management.app.authservice.exception.CustomException;
import cinema.management.app.authservice.mapper.UserMapper;
import cinema.management.app.authservice.repository.UserRepository;
import cinema.management.app.authservice.security.JwtTokenProvider;
import cinema.management.app.authservice.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final MessageSource messageSource;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto findCurrentUser(HttpServletRequest request) {
        final String email = getEmailFromHttpRequest(request);
        return userMapper.entityToDto(
                findByEmail(email)
        );
    }

    @Override
    public UserResponseDto updateCurrentUser(
            HttpServletRequest request,
            final UpdateUserRequestDto dto
    ) {
        final String email = getEmailFromHttpRequest(request);
        User user = findByEmail(email);

        user.setEmail(dto.email());
        user.setFirstName(dto.firstName());
        user.setLastName(dto.lastName());

        return userMapper.entityToDto(
                userRepository.update(user.getId(), user)
        );
    }

    private String getEmailFromHttpRequest(HttpServletRequest request) {
        String token;
        Cookie authCookie = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("AuthToken")) {
                    authCookie = cookie;
                }
            }
        }

        if (authCookie != null && !authCookie.getValue().isEmpty()) {
            token = authCookie.getValue();
        } else {
            token = request.getHeader(HttpHeaders.AUTHORIZATION);
        }

        if (token != null && token.startsWith(BEARER)) {
            token = token.substring(BEARER.length());
        }

        if (token != null && jwtTokenProvider.validateAccessToken(token)) {
            return jwtTokenProvider.getEmailFromAccessToken(token);
        }

        return null;
    }

    private User findByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "user.auth.errors.user.not.found.by.id",
                                new Object[]{email},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }
}
