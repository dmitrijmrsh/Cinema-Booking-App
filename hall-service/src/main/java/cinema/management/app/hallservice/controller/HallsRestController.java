package cinema.management.app.hallservice.controller;

import cinema.management.app.hallservice.dto.request.HallCreateRequestDto;
import cinema.management.app.hallservice.dto.response.HallResponseDto;
import cinema.management.app.hallservice.service.HallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/halls")
public class HallsRestController {

    private final HallService hallService;

    @GetMapping
    public List<HallResponseDto> findAllHalls() {
        return hallService.findAllHalls();
    }

    @GetMapping("/by-activeness")
    public List<HallResponseDto> findAllHallsWithCurrentActivityStatus(
            @RequestParam(name = "is_active") final Boolean activityStatus
    ) {
        return hallService.findAllHallsWithCurrentActivityStatus(activityStatus);
    }

    @PostMapping
    public ResponseEntity<?> createHall(
            @Valid @RequestBody HallCreateRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        HallResponseDto responseDto = hallService.saveHall(dto);
        String entityURI = "/api/v1/halls/" + responseDto.id();

        return ResponseEntity.created(URI.create(entityURI))
                .body(responseDto);
    }
}
