package cinema.management.app.authservice.controller;

import cinema.management.app.authservice.dto.request.UpdateUserRoleRequestDto;
import cinema.management.app.authservice.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/managers")
@RequiredArgsConstructor
public class ManagerRestController {

    private final ManagerService managerService;

    @GetMapping("/all-users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok()
                .body(managerService.getAllUsers());
    }

    @PatchMapping("/{userId:\\d+}")
    public ResponseEntity<?> changeUserRoleById(
            @PathVariable("userId") final Integer userId,
            @Valid @RequestBody UpdateUserRoleRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()){
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        managerService.setRoleToUserById(userId, dto);

        return ResponseEntity.noContent()
                .build();
    }

}
