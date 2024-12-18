package cinema.management.app.webclient.dto.screening.request;

public record SeatReserveRequestDto(

        Integer rowNumber,

        Integer seatInRow

) {
}
