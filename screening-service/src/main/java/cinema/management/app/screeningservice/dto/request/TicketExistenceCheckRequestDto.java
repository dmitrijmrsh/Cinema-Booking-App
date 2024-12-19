package cinema.management.app.screeningservice.dto.request;

public record TicketExistenceCheckRequestDto(

        Integer userId,

        Integer screeningId

) {
}
