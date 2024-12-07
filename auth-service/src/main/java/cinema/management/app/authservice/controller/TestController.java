package cinema.management.app.authservice.controller;

import cinema.management.app.authservice.entity.Role;
import cinema.management.app.authservice.entity.User;
import cinema.management.app.authservice.exception.CustomException;
import cinema.management.app.authservice.repository.RoleRepository;
import cinema.management.app.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth-test")
@RequiredArgsConstructor
public class TestController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @PostMapping("/users")
    public User saveUser() {
        User user =  userRepository.save(new User(
                -1,
                "Лионель",
                "Месси",
                "messi@example.com",
                "12345",
                new Role(
                        1,
                        "IDONTNEEDID"
                )
        ));
        return user;
    }

    @PutMapping("/users")
    public User updateUser() {
        return userRepository.update(1, new User(
                -1,
                "Джон",
                "Сноу",
                "snow@example.com",
                "dontmatter",
                new Role(
                        1,
                        "dontmatter"
                )
        ));
    }

    @GetMapping("/roles/{roleName}")
    public Role getRoleByName(@PathVariable("roleName") String roleName) {
        return roleRepository.findByName(roleName).orElseThrow(
                () -> new CustomException("Bad", HttpStatus.INTERNAL_SERVER_ERROR)
        );
    }

    @GetMapping("/roles/exists/{roleName}")
    public Boolean checkRoleExistsByName(@PathVariable("roleName") String roleName) {
        return roleRepository.existsByName(roleName);
    }

    @GetMapping("/users")
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{userId}")
    public User findUserById(@PathVariable("userId") Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException("Bad 2", HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @GetMapping("/users/exists-by-id/{userId}")
    public Boolean checkUserExistsById(@PathVariable("userId") Integer id) {
        return userRepository.existsById(id);
    }

    @GetMapping("/users/exists-by-name/{userEmail}")
    public Boolean checkUserExistsByEmail(@PathVariable("userEmail") String email) {
        return userRepository.existsByEmail(email);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUserById(@PathVariable("id") Integer id) {
        userRepository.deleteById(id);
    }

}
