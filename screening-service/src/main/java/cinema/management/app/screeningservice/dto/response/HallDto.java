package cinema.management.app.screeningservice.dto.response;

public record HallDto(

        Integer id,

        Boolean isActive,

        Integer rowCount,

        Integer seatInRowCount

) {
}
