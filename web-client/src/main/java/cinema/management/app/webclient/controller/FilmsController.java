package cinema.management.app.webclient.controller;

import cinema.management.app.webclient.client.auth.AuthRestClient;
import cinema.management.app.webclient.client.film.FilmRestClient;
import cinema.management.app.webclient.dto.auth.response.AccessTokenResponseDto;
import cinema.management.app.webclient.exception.AccessDeniedException;
import cinema.management.app.webclient.exception.BadRequestException;
import cinema.management.app.webclient.exception.FilmAlreadyExistException;
import cinema.management.app.webclient.exception.UserUnauthorizedException;
import cinema.management.app.webclient.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/profile/special-features/films")
@RequiredArgsConstructor
public class FilmsController {

    private final FilmRestClient filmRestClient;

    @GetMapping
    public String getFilmsPage(Model model) {
        try {
            model.addAttribute(
                    "films",
                    filmRestClient.findAllFilms()
            );
            return "films";
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        } catch (AccessDeniedException exception) {
            return "redirect:/";
        }
    }

    @GetMapping("/create")
    public String getCreateFilmPage() {
        return "create-film";
    }

    @PostMapping("/create")
    public String createFilm(
            @RequestParam final String title,
            @RequestParam final String genre,
            @RequestParam final Integer durationInMinutes,
            @RequestParam final String description,
            Model model
    ) {
        try {
            filmRestClient.createFilm(
                    title,
                    genre,
                    description,
                    durationInMinutes
            );
        } catch (UserUnauthorizedException exception) {
            return "redirect:/login";
        } catch (FilmAlreadyExistException exception) {
            model.addAttribute(
                    "error",
                    "Фильм с таким названием уже существует"
            );
            return "create-film";
        } catch (BadRequestException exception) {
            model.addAttribute(
                    "error",
                    "Некорректные данные для фильма"
            );
            return "create-film";
        } catch (AccessDeniedException exception) {
            return "redirect:/";
        }

        model.addAttribute(
                "films",
                filmRestClient.findAllFilms()
        );
        return "films";
    }
}
