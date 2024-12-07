package cinema.management.app.authservice.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    private Integer id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

}
