package cinema.management.app.screeningservice.service.impl;

import cinema.management.app.screeningservice.client.HallClient;
import cinema.management.app.screeningservice.dto.request.ScreeningCreationRequestDto;
import cinema.management.app.screeningservice.dto.response.FilmDto;
import cinema.management.app.screeningservice.dto.response.HallDto;
import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;
import cinema.management.app.screeningservice.exception.CustomException;
import cinema.management.app.screeningservice.client.FilmClient;
import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.entity.Seat;
import cinema.management.app.screeningservice.entity.enums.SeatStatus;
import cinema.management.app.screeningservice.mapper.ScreeningMapper;
import cinema.management.app.screeningservice.repository.ScreeningRepository;
import cinema.management.app.screeningservice.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningMapper screeningMapper;
    private final FilmClient filmClient;
    private final HallClient hallClient;
    private final MessageSource messageSource;

    @Override
    public List<ScreeningResponseDto> findAllScreenings() {
        List<ScreeningResponseDto> screenings = screeningRepository.findAll().stream()
                .map(screening -> new ScreeningResponseDto(
                                screening.getId(),
                                screening.getDate(),
                                screening.getTime(),
                                filmClient.findFilmById(screening.getFilmId()),
                                hallClient.findHallById(screening.getHallId())
                        )
                ).toList();

        log.info("Found screenings list of size {}", screenings.size());

        return screenings;
    }

    @Override
    public List<ScreeningResponseDto> findScreeningsByDate(LocalDate date) {
        List<ScreeningResponseDto> screeningsOnDate = screeningRepository.findScreeningsByDate(date).stream()
                .map(screening -> new ScreeningResponseDto(
                                screening.getId(),
                                screening.getDate(),
                                screening.getTime(),
                                filmClient.findFilmById(screening.getFilmId()),
                                hallClient.findHallById(screening.getHallId())
                        )
                ).toList();

        log.info("Found screenings list by date {} of size {}", date.toString(), screeningsOnDate.size());

        return screeningsOnDate;
    }

    @Override
    public List<ScreeningResponseDto> findScreeningsByFilmId(Integer id) {
        FilmDto film = filmClient.findFilmById(id);

        List<ScreeningResponseDto> screeningsByFilm = screeningRepository.findScreeningsByFilmId(id).stream()
                .map(screening -> new ScreeningResponseDto(
                        screening.getId(),
                        screening.getDate(),
                        screening.getTime(),
                        film,
                        hallClient.findHallById(screening.getHallId())
                    )
                ).toList();

        log.info("Found screenings list by film {} of size {}", film.title(), screeningsByFilm.size());

        return screeningsByFilm;
    }

    @Override
    public ScreeningResponseDto findScreeningById(Integer id) {
        Screening screening = findById(id);

        log.info("Found screening with id {}", screening.getId());

        return new ScreeningResponseDto(
                screening.getId(),
                screening.getDate(),
                screening.getTime(),
                filmClient.findFilmById(screening.getFilmId()),
                hallClient.findHallById(screening.getId())
        );
    }

    @Override
    @Transactional
    public ScreeningResponseDto saveScreening(ScreeningCreationRequestDto dto) {
        FilmDto film = filmClient.findFilmById(dto.filmId());
        HallDto hall = hallClient.findHallById(dto.hallId());

        if (!hall.isActive()) {
            throw new CustomException(messageSource.getMessage(
                    "screening.service.hall.is.inactive",
                    new Object[]{hall.id()},
                    LocaleContextHolder.getLocale()
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!screeningTimeFrameValidityCheck(
             dto.date(),
             dto.time(),
             film,
             hall
        )) {
            throw new CustomException(messageSource.getMessage(
                    "screening.service.screening.invalid.time.frame",
                    new Object[]{dto.date().toString()},
                    LocaleContextHolder.getLocale()
            ), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        Screening screening = screeningMapper.dtoToEntity(dto);
        screening.setSeats(createSeatsForScreening(hall.rowCount(), hall.seatInRowCount(), screening));

        screening = screeningRepository.save(screening);

        log.info("Saved screening with id {}", screening.getId());

        return new ScreeningResponseDto(
                screening.getId(),
                screening.getDate(),
                screening.getTime(),
                film,
                hall
        );
    }

    @Override
    @Transactional
    public void deleteScreening(Integer id) {
        screeningRepository.deleteById(id);

        log.info("Deleted screening with id {}", id);
    }

    private Screening findById(Integer id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "screening.service.not.found.screening.by.id",
                        new Object[]{id},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));
    }

    private List<Seat> createSeatsForScreening(
            Integer rowCount,
            Integer seatInRowCount,
            Screening screening
    ) {
        return IntStream.rangeClosed(1, rowCount)
                .boxed()
                .flatMap(rowNumber -> IntStream.rangeClosed(1, seatInRowCount)
                        .mapToObj(seatInRow -> {
                            Seat seat = new Seat();
                            seat.setRowNumber(rowNumber);
                            seat.setSeatInRow(seatInRow);
                            seat.setStatus(SeatStatus.AVAILABLE);
                            seat.setScreening(screening);
                            return seat;
                        }))
                .toList();
    }

    private boolean screeningTimeFrameValidityCheck(
            LocalDate date,
            LocalTime time,
            FilmDto film,
            HallDto hall
    ) {
        List<ScreeningResponseDto> screeningsDtoByHallIdAndDate = screeningRepository
                .findScreeningsByHallIdAndDate(hall.id(), date).stream()
                    .map(screening -> new ScreeningResponseDto(
                                    screening.getId(),
                                    screening.getDate(),
                                    screening.getTime(),
                                    filmClient.findFilmById(screening.getFilmId()),
                                    hallClient.findHallById(screening.getHallId())
                            )
                    )
                .toList();

        System.out.println(screeningsDtoByHallIdAndDate);

        if (screeningsDtoByHallIdAndDate.isEmpty()) {
            return true;
        }

        ScreeningResponseDto currentScreening;
        LocalTime currentScreeningStartTime;
        LocalTime currentScreeningEndTime;

        LocalTime screeningEndTime = time.plusMinutes((long) film.durationInMinutes());

        System.out.println(screeningEndTime);

        boolean mainTimeIntervalReversed = false;
        boolean currentTimeIntervalReversed = false;

        if (time.plusMinutes((long) film.durationInMinutes()).isBefore(time)) {
            mainTimeIntervalReversed = true;
        }

        System.out.println(mainTimeIntervalReversed);

        for (ScreeningResponseDto screeningResponseDto : screeningsDtoByHallIdAndDate) {
            currentScreening = screeningResponseDto;
            currentScreeningStartTime = currentScreening.time();
            currentScreeningEndTime = currentScreeningStartTime.plusMinutes(
                    (long) currentScreening.filmDto().durationInMinutes()
            );

            if (currentScreeningEndTime.isBefore(currentScreeningStartTime)) {
                currentTimeIntervalReversed = true;
            }

            if (!mainTimeIntervalReversed && !currentTimeIntervalReversed) {
                System.out.println("First");
                if (
                        (
                                (time.isAfter(currentScreeningStartTime)
                                        || time.equals(currentScreeningStartTime))
                                &&
                                (time.isBefore(currentScreeningEndTime)
                                        || time.equals(currentScreeningEndTime))
                        )
                                ||
                        (
                                (screeningEndTime.isAfter(currentScreeningStartTime)
                                        || screeningEndTime.equals(currentScreeningStartTime))
                                &&
                                (screeningEndTime.isBefore(currentScreeningEndTime)
                                        || screeningEndTime.equals(currentScreeningEndTime))
                        )
                ) {
                    return false;
                }
            }

            if (mainTimeIntervalReversed && currentTimeIntervalReversed) {
                System.out.println("Second");
                if (
                        (
                                (time.isAfter(currentScreeningEndTime)
                                        || time.equals(currentScreeningEndTime))
                                &&
                                (time.isBefore(currentScreeningStartTime)
                                        || time.equals(currentScreeningStartTime))
                        )
                                ||
                        (
                                (screeningEndTime.isAfter(currentScreeningEndTime)
                                        || screeningEndTime.equals(currentScreeningEndTime))
                                &&
                                (screeningEndTime.isBefore(currentScreeningStartTime)
                                        || screeningEndTime.equals(currentScreeningStartTime))
                        )
                ) {
                    return false;
                }
            }

            if (!mainTimeIntervalReversed && currentTimeIntervalReversed) {
                System.out.println("Third");
                if (
                        (time.isBefore(currentScreeningEndTime)
                                || time.equals(currentScreeningEndTime))
                        ||
                        (screeningEndTime.isAfter(currentScreeningStartTime)
                                || screeningEndTime.equals(currentScreeningStartTime))
                ) {
                    return false;
                }
            }

            if (mainTimeIntervalReversed && !currentTimeIntervalReversed) {
                System.out.println("Fourth");
                if (
                        (currentScreeningStartTime.isBefore(screeningEndTime)
                                || currentScreeningStartTime.equals(screeningEndTime))
                        ||
                        (currentScreeningEndTime.isAfter(time)
                                || currentScreeningEndTime.equals(time))
                ) {
                    return false;
                }
            }
        }

        return true;
    }

}
