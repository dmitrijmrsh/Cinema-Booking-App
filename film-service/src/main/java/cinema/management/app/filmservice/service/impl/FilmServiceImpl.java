package cinema.management.app.filmservice.service.impl;

import cinema.management.app.filmservice.dto.request.FilmCreationRequestDto;
import cinema.management.app.filmservice.dto.request.FilmUpdateRequestDto;
import cinema.management.app.filmservice.dto.response.FilmResponseDto;
import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.entity.Genre;
import cinema.management.app.filmservice.exception.CustomException;
import cinema.management.app.filmservice.mapper.FilmMapper;
import cinema.management.app.filmservice.repository.FilmRepository;
import cinema.management.app.filmservice.repository.GenreRepository;
import cinema.management.app.filmservice.service.FilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Log4j2
@Service
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final GenreRepository genreRepository;
    private final FilmMapper filmMapper;
    private final MessageSource messageSource;

    @Override
    public List<FilmResponseDto> findAllFilms() {
        log.info("Returning all films");
        return filmRepository.findAll().stream()
                .map(filmMapper::entityToDto)
                .toList();
    }

    @Override
    public FilmResponseDto findFilmById(Integer id) {
        Film film = findById(id);

        log.info("Found film with ID {}", id);

        return filmMapper.entityToDto(film);
    }

    @Override
    public List<FilmResponseDto> findFilmByGenre(String genreName) {
        List<Film> films = filmRepository.findByGenreName(genreName);

        log.info("Found {} films by genre {}.", films.size(), genreName);

        return films.stream()
                .map(filmMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public FilmResponseDto saveFilm(FilmCreationRequestDto dto) {
        String title = dto.title();

        if (filmRepository.existsByTitle(dto.title())) {
            throw new CustomException(messageSource.getMessage(
                    "film.service.errors.film.already.exists",
                    new Object[]{title},
                    LocaleContextHolder.getLocale()
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Film film = filmMapper.dtoToEntity(dto);

        Optional<Genre> mayBeGenre = genreRepository.findByName(dto.genre());
        mayBeGenre.ifPresent(film::setGenre);

        film = filmRepository.save(film);

        log.info("Saved film {}", film.getTitle());

        return filmMapper.entityToDto(film);
    }

    @Override
    @Transactional
    public FilmResponseDto updateFilm(Integer id, FilmUpdateRequestDto dto) {
        Film film = findById(id);

        film.setTitle(dto.title());
        film.setDescription(dto.description());
        film.setDurationInMinutes(dto.durationInMinutes());

        Optional<Genre> mayBeGenre = genreRepository.findByName(dto.genre());
        mayBeGenre.ifPresentOrElse(
                film::setGenre,
                () -> {
                    Genre genre = new Genre();
                    genre.setName(dto.genre());
                    film.setGenre(genre);
                }
        );

        Film updatedFilm = filmRepository.save(film);

        return filmMapper.entityToDto(updatedFilm);
    }

    @Override
    @Transactional
    public void deleteFilm(Integer id) {
        filmRepository.deleteById(id);
        log.info("Deleted film with id {}", id);
    }

    private Film findById(Integer id) {
        return filmRepository.findById(id)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "film.service.errors.film.not.found",
                        new Object[] {id},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));
    }
    
}
