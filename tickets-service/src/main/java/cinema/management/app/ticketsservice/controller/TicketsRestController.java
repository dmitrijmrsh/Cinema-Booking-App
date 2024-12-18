package cinema.management.app.ticketsservice.controller;

import cinema.management.app.ticketsservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.ticketsservice.dto.response.TicketResponseDto;
import cinema.management.app.ticketsservice.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketsRestController {

    private final TicketService ticketService;

    @GetMapping("/by-user/{userId:\\d+}")
    public List<TicketResponseDto> findAllTicketsByUserId(
            @PathVariable("userId") final Integer userId
    ) {
        return ticketService.findAllTicketsByUserId(userId);
    }

    @GetMapping("/exists")
    public Boolean existsByUserIdAndScreeningId(
            @RequestParam("userId") final Integer userId,
            @RequestParam("screeningId") final Integer screeningId
    ) {
        return ticketService.existsByUserIdAndScreeningId(userId, screeningId);
    }

    @PostMapping
    public ResponseEntity<?> createTicket(
            @Valid @RequestBody TicketCreationRequestDto dto,
            BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception) {
                throw exception;
            }

            throw new BindException(bindingResult);
        }

        TicketResponseDto responseDto = ticketService.saveTicket(dto);

        return ResponseEntity.ok()
                .body(responseDto);
    }

    @DeleteMapping("/{ticketId:\\d+}")
    public ResponseEntity<?> deleteTicket(
            @PathVariable("ticketId") final Integer ticketId
    ) {
        ticketService.deleteTicketById(ticketId);
        return ResponseEntity.noContent()
                .build();
    }

}
