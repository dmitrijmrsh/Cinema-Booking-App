package cinema.management.app.filmservice.service.impl;

import cinema.management.app.filmservice.dto.request.GenreCreationRequestDto;
import cinema.management.app.filmservice.dto.response.GenreResponseDto;
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

    private final MessageSource messageSource;
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public List<GenreResponseDto> findAllGenres() {
        log.info("Getting genres list");
        return genreRepository.findAll().stream()
                .map(genreMapper::entityToDto)
                .toList();
    }

    @Override
    public GenreResponseDto findGenreById(Integer id) {
        log.info("Getting genre with id: {}", id);
        return genreRepository.findById(id)
                .map(genreMapper::entityToDto)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "film.service.errors.genre.not.found",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));
    }

    @Override
    @Transactional
    public GenreResponseDto saveGenre(GenreCreationRequestDto dto) {
        log.info("Saving genre with name: {}", dto.name());

        String genreName = dto.name();

        if (genreRepository.existsByName(genreName)) {
            throw new CustomException(
                    this.messageSource.getMessage(
                            "film.service.errors.genre.already.exist",
                            new Object[]{genreName},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.CONFLICT
            );
        }

        return genreMapper.entityToDto(
                genreRepository.save(genreMapper.dtoToEntity(dto))
        );
    }
}
