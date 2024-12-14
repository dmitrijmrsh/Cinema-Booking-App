package cinema.management.app.screeningservice.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Screening {

    private Integer id;
    private LocalDate date;
    private LocalTime time;
    private Integer filmId;
    private Integer hallId;
    private List<Seat> seats;

}
