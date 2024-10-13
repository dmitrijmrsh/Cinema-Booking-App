package cinema.management.app.screeningservice;

import cinema.management.app.screeningservice.client.FilmClient;
import cinema.management.app.screeningservice.client.HallClient;
import cinema.management.app.screeningservice.dto.response.FilmDto;
import cinema.management.app.screeningservice.dto.response.HallDto;
import jakarta.validation.constraints.AssertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = TestBeans.class)
class ScreeningServiceApplicationTests {

    @Autowired
    private HallClient hallClient;

    @Autowired
    private FilmClient filmClient;

    @Test
    @Transactional
    void contextLoads() throws Exception {
        HallDto hall = hallClient.findHallById(1);

        assertTrue(hall.isActive() && hall.rowCount().equals(10) && hall.seatInRowCount().equals(10));
    }

}
