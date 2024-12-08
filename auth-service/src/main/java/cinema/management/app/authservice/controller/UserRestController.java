package cinema.management.app.authservice.controller;

import cinema.management.app.authservice.dto.request.UpdateUserRequestDto;
import cinema.management.app.authservice.dto.response.UserResponseDto;
import cinema.management.app.authservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @GetMapping("/{userId:\\d+}")
    public ResponseEntity<?> getUserInfoById(
            @PathVariable("userId") final Integer userId
    ) {
        UserResponseDto userResponseDto = userService.findUserById(userId);
        return ResponseEntity.ok()
                .body(userResponseDto);
    }

    @PatchMapping("/{userId:\\d+}")
    public ResponseEntity<?> updateUserDataById(
            @PathVariable("userId") final Integer userId,
            @Valid @RequestBody UpdateUserRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        UserResponseDto responseDto = userService.updateUserById(
                userId, dto
        );

        return ResponseEntity.ok()
                .body(responseDto);

    }

    @DeleteMapping("{userId:\\d+}")
    public ResponseEntity<?> deleteUserById(
            @PathVariable("userId") final Integer userId
    ) {
        userService.deleteUserById(userId);
        return ResponseEntity.noContent()
                .build();
    }
}
