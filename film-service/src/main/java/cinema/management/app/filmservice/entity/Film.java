package cinema.management.app.filmservice.entity;

import cinema.management.app.filmservice.entity.enums.FilmCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "film")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private FilmCategory category;

    @Column(name = "duration_film_in_minutes")
    private Integer durationFilmInMinutes;
}
