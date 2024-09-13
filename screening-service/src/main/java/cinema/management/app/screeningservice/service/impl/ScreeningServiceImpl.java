package cinema.management.app.screeningservice.service.impl;

import cinema.management.app.filmservice.entity.Film;
import cinema.management.app.filmservice.exception.CustomException;
import cinema.management.app.screeningservice.client.FilmClient;
import cinema.management.app.screeningservice.dto.ScreeningAvailableSeatsDto;
import cinema.management.app.screeningservice.dto.ScreeningRequestDto;
import cinema.management.app.screeningservice.dto.ScreeningResponseDto;
import cinema.management.app.screeningservice.dto.SeatReservingRequestDto;
import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.entity.Seat;
import cinema.management.app.screeningservice.entity.enums.SeatStatus;
import cinema.management.app.screeningservice.mapper.ScreeningMapper;
import cinema.management.app.screeningservice.repository.ScreeningRepository;
import cinema.management.app.screeningservice.repository.SeatRepository;
import cinema.management.app.screeningservice.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ScreeningMapper screeningMapper;
    private final FilmClient filmClient;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public ScreeningResponseDto saveScreening(ScreeningRequestDto screeningRequestDto, Long filmId) {
        Film film = filmClient.findFilmById(filmId);

        Screening screening = screeningMapper.dtoToEntity(screeningRequestDto);
        screening.setFilmId(film.getId());

        List<Seat> screeningSeats = IntStream.rangeClosed(1, 10)
                .boxed()
                .flatMap(rowNumber -> IntStream.rangeClosed(1, 10)
                        .mapToObj(seatInRow -> {
                            Seat seat = new Seat();
                            seat.setRowsNumber(rowNumber);
                            seat.setSeatInRow(seatInRow);
                            seat.setStatus(SeatStatus.AVAILABLE);
                            return seat;
                        }))
                .toList();

        screening.setSeats(screeningSeats);

        Screening savedScreening = screeningRepository.save(screening);

        return new ScreeningResponseDto(
                savedScreening.getId(),
                savedScreening.getDate(),
                savedScreening.getTime(),
                film
        );
    }

    @Override
    @Transactional
    public ScreeningResponseDto findScreeningById(Long screeningId) {
        Screening screening = getScreeningById(screeningId);
        Film film = filmClient.findFilmById(screening.getFilmId());
        return new ScreeningResponseDto(
                screening.getId(),
                screening.getDate(),
                screening.getTime(),
                film
        );
    }

    @Override
    @Transactional
    public List<ScreeningResponseDto> findScreeningsByDate(LocalDate date) {
        return screeningRepository.findScreeningsByDate(date).stream()
                .map(screening -> {
                    Film film = filmClient.findFilmById(screening.getId());
                    return new ScreeningResponseDto(
                            screening.getId(),
                            screening.getDate(),
                            screening.getTime(),
                            film
                    );
                })
                .toList();
    }

    @Override
    public ScreeningAvailableSeatsDto findAvailableSeats(Long screeningId) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "screening.service.not.found.screening.by.id",
                        new Object[]{screeningId},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));

        return screeningMapper.entityToSeatsDto(screening);
    }

    @Override
    public void reserveSeat(SeatReservingRequestDto seatReservingRequestDto) {
        Long screeningId = seatReservingRequestDto.screeningId();
        Integer rowsNumber = seatReservingRequestDto.rowsNumber();
        Integer seatInRow = seatReservingRequestDto.seatInRow();

        Screening screening = getScreeningById(screeningId);

        Seat seat = seatRepository.findByScreeningAndRowsNumberAndSeatInRow(screening, rowsNumber, seatInRow)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "screening.service.not.found.seat.in.screening.by.rows.number.and.seat.in.row",
                        new Object[]{screeningId, rowsNumber, seatInRow},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));

        if (seat.getStatus() == SeatStatus.AVAILABLE) {
            seat.setStatus(SeatStatus.TAKEN);
            seatRepository.save(seat);
            return;
        }

        throw new CustomException(messageSource.getMessage(
                "screening.service.seat.is.already.taken",
                new Object[]{screeningId, rowsNumber, seatInRow},
                LocaleContextHolder.getLocale()
        ), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    private Screening getScreeningById(Long screeningId) {
        return screeningRepository.findById(screeningId)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "screening.service.not.found.screening.by.id",
                        new Object[]{screeningId},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));
    }
}
