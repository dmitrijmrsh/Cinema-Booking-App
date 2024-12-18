package cinema.management.app.webclient.dto.hall.response;

public record HallResponseDto(

        Integer id,

        Boolean isActive,

        Integer rowCount,

        Integer seatInRowCount

) {
}
