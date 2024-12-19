package cinema.management.app.webclient.dto.hall.request;

public record HallCreateRequestDto(
        Boolean isActive,
        Integer rowCount,
        Integer seatInRowCount
) {
}
