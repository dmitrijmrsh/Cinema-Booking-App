package cinema.management.app.screeningservice.dto.request;

public record TicketCreationRequestDto(

        Integer userId,

        Integer screeningId,

        Integer seatId

) {
}
