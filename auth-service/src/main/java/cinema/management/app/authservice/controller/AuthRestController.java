package cinema.management.app.authservice.controller;

import cinema.management.app.authservice.dto.request.RefreshTokenRequestDto;
import cinema.management.app.authservice.dto.request.UserLogInRequestDto;
import cinema.management.app.authservice.dto.request.UserSignUpRequestDto;
import cinema.management.app.authservice.dto.response.AccessTokenResponseDto;
import cinema.management.app.authservice.dto.response.UserLogInResponseDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody UserSignUpRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        UserResponseDto responseDto = authService.signUp(dto);
        String entityURI = "/api/v1/users/" + responseDto.id();

        return ResponseEntity.created(URI.create(entityURI))
                .body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> logIn(
            @Valid @RequestBody UserLogInRequestDto dto,
            BindingResult bindingResult,
            HttpServletResponse httpServletResponse
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        UserLogInResponseDto responseDto = authService.logIn(
                dto,
                httpServletResponse
        );

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @PostMapping("/token")
    public ResponseEntity<?> getAccessToken(
            @Valid @RequestBody RefreshTokenRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        AccessTokenResponseDto responseDto = authService.getAccessToken(dto);

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logOut(HttpServletResponse httpServletResponse) {
        authService.logOut(httpServletResponse);
        return ResponseEntity.noContent()
                .build();
    }
}
