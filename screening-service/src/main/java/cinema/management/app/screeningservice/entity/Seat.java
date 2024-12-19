package cinema.management.app.screeningservice.entity;

import cinema.management.app.screeningservice.constant.SeatStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Seat {

    private Integer id;

    @JsonProperty("row_number")
    private Integer rowNumber;

    @JsonProperty("seat_in_row")
    private Integer seatInRow;

    private SeatStatus status;

    @JsonProperty("screening_id")
    private Integer screeningId;

}
