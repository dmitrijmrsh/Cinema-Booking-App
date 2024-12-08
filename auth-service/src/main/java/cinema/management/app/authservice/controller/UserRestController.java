package cinema.management.app.authservice.controller;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getCurrentUserInfo(
            HttpServletRequest request
    ) {
        UserResponseDto userResponseDto = userService.findCurrentUser(request);
        return ResponseEntity.ok()
                .body(userResponseDto);
    }

    @PatchMapping
    public ResponseEntity<?> updateCurrentUserData(
            HttpServletRequest request,
            @Valid @RequestBody UpdateUserRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        UserResponseDto responseDto = userService.updateCurrentUser(
                request, dto
        );

        return ResponseEntity.ok()
                .body(responseDto);

    }
}
