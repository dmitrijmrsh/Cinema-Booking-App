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
        log.info("Getting all films");
        return filmRepository.findAll().stream()
                .map(filmMapper::entityToDto)
                .toList();
    }

    @Override
    public List<FilmResponseDto> findFilmByGenre(final String genreName) {
        log.info("Getting all films by genre: {}", genreName);
        return filmRepository.findAllByGenreName(genreName).stream()
                .map(filmMapper::entityToDto)
                .toList();
    }

    @Override
    public FilmResponseDto findFilmById(final Integer id) {
        log.info("Getting film with id: {}", id);
        return filmRepository.findById(id)
                .map(filmMapper::entityToDto)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "film.service.errors.film.not.found",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }

    @Override
    @Transactional
    public FilmResponseDto saveFilm(final FilmCreationRequestDto dto) {
        log.info("Saving film with name: {}", dto.title());

        if (filmRepository.existsByTitle(dto.title())) {
            throw new CustomException(
                    this.messageSource.getMessage(
                            "film.service.errors.film.already.exists",
                            new Object[]{dto.title()},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.CONFLICT
            );
        }

        Film film = filmMapper.dtoToEntity(dto);
        setGenreToFilm(film, dto.genre());

        return filmMapper.entityToDto(filmRepository.save(film));
    }

    @Override
    @Transactional
    public FilmResponseDto updateFilm(final Integer id, final FilmUpdateRequestDto dto) {
        log.info("Updating film with id: {}", id);

        if (!filmRepository.existsById(id)) {
            throw new CustomException(
                    this.messageSource.getMessage(
                            "film.service.errors.film.not.found",
                            new Object[]{id},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.NOT_FOUND
            );
        }

        Film film = filmMapper.dtoToEntity(dto);
        setGenreToFilm(film, dto.genre());

        return filmMapper.entityToDto(
                filmRepository.update(id, film)
        );
    }

    @Override
    @Transactional
    public void deleteFilm(final Integer id) {
        filmRepository.delete(id);
    }

    private void setGenreToFilm(Film film,  final String genreName) {
        film.setGenre(
                genreRepository.findByName(genreName).orElseGet(
                        () -> genreRepository.save(new Genre(
                                -1,
                                genreName
                        ))
                )
        );
    }
}
