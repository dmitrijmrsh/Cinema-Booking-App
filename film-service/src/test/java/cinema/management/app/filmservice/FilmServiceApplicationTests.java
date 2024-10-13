package cinema.management.app.filmservice;

import cinema.management.app.filmservice.dto.request.FilmCreationRequestDto;
import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.mapper.FilmMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestBeans.class)
class FilmServiceApplicationTests {

    @Autowired
    private FilmMapper filmMapper;

    @Test
    void contextLoads() {
        FilmCreationRequestDto dto = new FilmCreationRequestDto(
                "dfdhjfj",
                "bebra",
                "ddsdsd",
                110
        );

        Film film = filmMapper.dtoToEntity(dto);

        System.out.println(film.getGenre().getId() + " " + film.getGenre().getName());
    }

}
