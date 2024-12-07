package cinema.management.app.filmservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Film {

    private Integer id;
    private String title;
    private String description;
    private Integer durationInMinutes;
    private Genre genre;

}
