package cinema.management.app.screeningservice.entity;

import cinema.management.app.screeningservice.entity.enums.SeatStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seats")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rows_number")
    private Integer rowsNumber;

    @Column(name = "seat_in_row")
    private Integer seatInRow;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "screening_id")
    @JsonIgnore
    private Screening screening;
}
