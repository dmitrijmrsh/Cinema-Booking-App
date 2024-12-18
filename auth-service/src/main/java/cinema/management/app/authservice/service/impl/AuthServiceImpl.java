package cinema.management.app.authservice.service.impl;

import cinema.management.app.authservice.dto.request.RefreshTokenRequestDto;
import cinema.management.app.authservice.dto.request.UserLogInRequestDto;
import cinema.management.app.authservice.dto.request.UserSignUpRequestDto;
import cinema.management.app.authservice.dto.response.AccessTokenResponseDto;
import cinema.management.app.authservice.dto.response.UserLogInResponseDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.entity.EmailToRefreshToken;
import cinema.management.app.authservice.entity.User;
import cinema.management.app.authservice.exception.CustomException;
import cinema.management.app.authservice.mapper.UserMapper;
import cinema.management.app.authservice.repository.RoleRepository;
import cinema.management.app.authservice.repository.UserRefreshTokenRepository;
import cinema.management.app.authservice.repository.UserRepository;
import cinema.management.app.authservice.security.JwtTokenProvider;
import cinema.management.app.authservice.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static cinema.management.app.authservice.constant.Roles.ADMIN;
import static cinema.management.app.authservice.constant.Roles.USER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Value("${security.admin.email}")
    private String adminEmail;

    @Value("${security.admin.password}")
    private String adminPassword;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRefreshTokenRepository refreshStorage;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public UserResponseDto signUp(UserSignUpRequestDto dto) {
        User user = userMapper.dtoToEntity(dto);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new CustomException(
                    this.messageSource.getMessage(
                    "user.auth.errors.email.already.exists",
                          new Object[]{user.getEmail()},
                          LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        if (user.getEmail().equals(adminEmail) && user.getPassword().equals(adminPassword)) {
            user.setRole(
                    roleRepository.findByName(ADMIN.getRoleInString())
                            .orElseThrow(() -> new CustomException(
                                    this.messageSource.getMessage(
                                     "user.auth.errors.role.not.found.by.name",
                                           new Object[]{ADMIN.getRoleInString()},
                                           LocaleContextHolder.getLocale()
                                    ),
                                    HttpStatus.NOT_FOUND
                            ))
            );
        } else {
            user.setRole(
                    roleRepository.findByName(USER.getRoleInString())
                            .orElseThrow(() -> new CustomException(
                                    this.messageSource.getMessage(
                                    "user.auth.errors.role.not.found.by.name",
                                          new Object[]{USER.getRoleInString()},
                                          LocaleContextHolder.getLocale()
                                    ),
                                    HttpStatus.NOT_FOUND
                            ))
            );
        }

        String passwordEncoded = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordEncoded);

        user = userRepository.save(user);

        return userMapper.entityToDto(user);
    }

    @Override
    @Transactional
    public UserLogInResponseDto logIn(
            UserLogInRequestDto dto,
            HttpServletResponse httpServletResponse
    ) {
        final String email = dto.email();
        final String rawPassword = dto.password();

        final User user = findUserByEmail(email);

        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
            final String accessToken = jwtTokenProvider.generateAccessToken(user);
            final String refreshToken = jwtTokenProvider.generateRefreshToken(user);

            refreshStorage.save(new EmailToRefreshToken(
                    user.getEmail(),
                    refreshToken
            ));

            setAuthTokenCookie(
                    httpServletResponse,
                    accessToken
            );

            return new UserLogInResponseDto(
                    accessToken,
                    refreshToken
            );
        }

        throw new CustomException(
                this.messageSource.getMessage(
                        "user.auth.errors.incorrect.password",
                        new Object[]{user.getEmail()},
                        LocaleContextHolder.getLocale()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    @Override
    public void logOut(HttpServletResponse httpServletResponse) {
        setAuthTokenCookie(
                httpServletResponse,
                ""
        );
    }

    @Override
    public AccessTokenResponseDto getAccessToken(RefreshTokenRequestDto dto) {
        final String refreshToken = dto.refreshToken();

        if (jwtTokenProvider.validateRefreshToken(refreshToken)) {
            final String email = jwtTokenProvider.getEmailFromRefreshToken(refreshToken);
            final String savedRefreshToken = getSavedRefreshTokenFromEmail(email);

            if (savedRefreshToken != null && savedRefreshToken.equals(refreshToken)) {
                final User user = findUserByEmail(email);

                final String accessToken = jwtTokenProvider.generateAccessToken(user);

                return new AccessTokenResponseDto(accessToken);
            }
        }

        throw new CustomException(
                this.messageSource.getMessage(
                        "user.auth.errors.invalid.refresh.token",
                        new Object[0],
                        LocaleContextHolder.getLocale()
                ),
                HttpStatus.UNAUTHORIZED
        );
    }

    private void setAuthTokenCookie(
            HttpServletResponse httpServletResponse,
            final String accessToken
    ) {
        Cookie cookie = new Cookie("AuthToken", accessToken);

        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(3600);

        httpServletResponse.addCookie(cookie);
    }

    private String getSavedRefreshTokenFromEmail(final String email) {
        final EmailToRefreshToken emailToRefreshToken = refreshStorage.findById(email)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "user.auth.errors.no.refresh.token",
                                new Object[]{email},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.INTERNAL_SERVER_ERROR
                ));
        return emailToRefreshToken.getRefreshToken();
    }

    private User findUserByEmail(final String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "user.auth.errors.user.not.found.by.email",
                                new Object[]{email},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }
}
