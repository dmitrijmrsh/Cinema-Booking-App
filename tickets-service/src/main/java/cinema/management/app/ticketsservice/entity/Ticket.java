package cinema.management.app.ticketsservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Ticket {

    private Integer id;
    private Integer userId;
    private Integer screeningId;
    private Integer seatId;

}
