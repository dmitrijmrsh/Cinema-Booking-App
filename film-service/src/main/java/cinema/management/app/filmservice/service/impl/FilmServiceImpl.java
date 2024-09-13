package cinema.management.app.filmservice.service.impl;

import cinema.management.app.filmservice.dto.CreatedFilmDto;
import cinema.management.app.filmservice.dto.FilmRequestDto;
import cinema.management.app.filmservice.dto.FilmResponseDto;
import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.entity.enums.FilmCategory;
import cinema.management.app.filmservice.exception.CustomException;
import cinema.management.app.filmservice.mapper.FilmMapper;
import cinema.management.app.filmservice.repository.FilmRepository;
import cinema.management.app.filmservice.service.FilmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class FilmServiceImpl implements FilmService {

    private final FilmRepository filmRepository;
    private final FilmMapper filmMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public CreatedFilmDto save(FilmRequestDto filmRequestDto) {
        String title = filmRequestDto.title();

        if (filmRepository.existsByTitle(filmRequestDto.title())) {
            throw new CustomException(messageSource.getMessage(
                    "film.service.logic.errors.film.already.exists",
                    new Object[]{title},
                    LocaleContextHolder.getLocale()
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

            Film film = filmRepository.save(filmMapper.dtoToEntity(filmRequestDto));

            log.info("Saved film {}", filmRequestDto);

            return filmMapper.createdEntityToDto(film);
    }

    @Override
    public FilmResponseDto findById(Long id) {
        Film film = filmRepository.findById(id)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "film.service.logic.errors.film.not.found",
                        new Object[] {id},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));

        log.info("Found film with ID {}", id);

        return filmMapper.entityToDto(film);
    }

    @Override
    public List<FilmResponseDto> findAll() {
        log.info("Returning all films");
        return filmRepository.findAll().stream()
                .map(filmMapper::entityToDto)
                .toList();
    }

    @Override
    public List<FilmResponseDto> findByCategory(FilmCategory filmCategory) {
        List<Film> films = filmRepository.findByCategory(filmCategory);

        log.info("Found {} films.", films.size());
        log.info("Returning all films by category {}", filmCategory);

        return films.stream()
                .map(filmMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        filmRepository.deleteById(id);
    }
}
