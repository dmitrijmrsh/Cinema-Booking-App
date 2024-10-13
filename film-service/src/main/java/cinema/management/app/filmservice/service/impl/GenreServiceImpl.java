package cinema.management.app.filmservice.service.impl;

import cinema.management.app.filmservice.dto.request.GenreCreationRequestDto;
import cinema.management.app.filmservice.dto.response.GenreResponseDto;
import cinema.management.app.filmservice.entity.Genre;
import cinema.management.app.filmservice.exception.CustomException;
import cinema.management.app.filmservice.mapper.GenreMapper;
import cinema.management.app.filmservice.repository.GenreRepository;
import cinema.management.app.filmservice.service.GenreService;
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
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final MessageSource messageSource;

    @Override
    public List<GenreResponseDto> findAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::entityToDto)
                .toList();
    }

    @Override
    public GenreResponseDto findGenreById(Integer id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "film.service.errors.genre.not.found",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));

        log.info("Found genre with id {}", id);

        return genreMapper.entityToDto(genre);
    }

    @Override
    @Transactional
    public GenreResponseDto saveGenre(GenreCreationRequestDto dto) {
        String genreName = dto.name();

        if (genreRepository.existsByName(genreName)) {
            throw new CustomException(messageSource.getMessage(
                    "film.service.errors.genre.already.exist",
                    new Object[]{genreName},
                    LocaleContextHolder.getLocale()
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Genre genre = genreRepository.save(genreMapper.dtoToEntity(dto));

        return genreMapper.entityToDto(genre);
    }

}
