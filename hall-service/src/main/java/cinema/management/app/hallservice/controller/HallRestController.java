package cinema.management.app.hallservice.controller;

import cinema.management.app.hallservice.dto.request.HallUpdateActivityStatusRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateRequestDto;
import cinema.management.app.hallservice.dto.response.HallResponseDto;
import cinema.management.app.hallservice.service.HallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/halls/{hallId:\\d+}")
public class HallRestController {

    private final HallService hallService;

    @GetMapping
    public HallResponseDto findHallById(
            @PathVariable("hallId") final Integer hallId
    ) {
        return hallService.findHallById(hallId);
    }

    @PatchMapping
    public ResponseEntity<?> changeHallActivityStatus(
            @PathVariable("hallId") final Integer id,
            @Valid @RequestBody HallUpdateActivityStatusRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        HallResponseDto responseDto = hallService.updateHallActivityStatus(id, dto);

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @PutMapping
    public ResponseEntity<?> updateHallData(
            @PathVariable("hallId") final Integer id,
            @Valid @RequestBody HallUpdateRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {

        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        HallResponseDto responseDto = hallService.updateHall(id, dto);

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteHall(
            @PathVariable("hallId") final Integer id
    ) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent()
                .build();
    }

}
