package cinema.management.app.ticketsservice.dto.response;

public record HallDto(

        Integer id,

        Boolean isActive,

        Integer rowCount,

        Integer seatInRowCount

) {
}
