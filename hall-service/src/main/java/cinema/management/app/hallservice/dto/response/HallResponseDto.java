package cinema.management.app.hallservice.dto.response;


public record HallResponseDto(

        Integer id,

        Boolean isActive,

        Integer rowCount,

        Integer seatInRowCount

) {
}
