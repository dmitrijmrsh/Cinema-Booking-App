package cinema.management.app.webclient.client.hall;

import cinema.management.app.webclient.dto.hall.response.HallResponseDto;

import java.util.List;

public interface HallRestClient {

    String HALL_BASE_URI = "http://localhost:8222/api/v1/halls";

    List<HallResponseDto> findAllHalls();

    void createHall(
            final Integer rowCount,
            final Integer seatInRowCount
    );

}
