package cinema.management.app.hallservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Hall {

    private Integer id;
    private Boolean isActive;
    private Integer rowCount;
    private Integer seatInRowCount;

}
