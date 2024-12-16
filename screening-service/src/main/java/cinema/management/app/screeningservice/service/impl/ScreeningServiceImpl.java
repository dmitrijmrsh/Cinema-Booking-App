package cinema.management.app.screeningservice.service.impl;

import cinema.management.app.screeningservice.client.FilmClient;
import cinema.management.app.screeningservice.client.HallClient;
import cinema.management.app.screeningservice.constant.SeatStatus;
import cinema.management.app.screeningservice.dto.request.ScreeningCreationRequestDto;
import cinema.management.app.screeningservice.dto.response.FilmDto;
import cinema.management.app.screeningservice.dto.response.HallDto;
import cinema.management.app.screeningservice.dto.response.ScreeningResponseDto;
import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.entity.Seat;
import cinema.management.app.screeningservice.exception.CustomException;
import cinema.management.app.screeningservice.mapper.ScreeningMapper;
import cinema.management.app.screeningservice.mapper.SeatMapper;
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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class ScreeningServiceImpl implements ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final ScreeningMapper screeningMapper;
    private final FilmClient filmClient;
    private final HallClient hallClient;
    private final MessageSource messageSource;

    @Override
    public List<ScreeningResponseDto> findAllScreenings() {
        List<ScreeningResponseDto> screenings = screeningRepository.findAll().stream()
                .map(screeningToDto)
                .toList();

        log.info("Found screenings list of size {}", screenings.size());

        return screenings;
    }

    @Override
    public List<ScreeningResponseDto> findScreeningsByDate(final LocalDate date) {
        List<ScreeningResponseDto> screenings = screeningRepository.findAllByDate(Date.valueOf(date)).stream()
                .map(screeningToDto)
                .toList();

        log.info("Found screenings list of size {} by date {}", screenings.size(), date.toString());

        return screenings;
    }

    @Override
    public List<ScreeningResponseDto> findScreeningsByFilmId(final Integer filmId) {
        List<ScreeningResponseDto> screenings = screeningRepository.findAllByFilmId(filmId).stream()
                .map(screeningToDto)
                .toList();

        log.info("Found screenings list of size {} by film id {}", screenings.size(), filmId);

        return screenings;
    }

    @Override
    public ScreeningResponseDto findScreeningById(final Integer id) {
        ScreeningResponseDto screening = screeningRepository.findById(id)
                .map(screeningToDto)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "screening.service.not.found.screening.by.id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));

        log.info("Found screening with id {}", id);

        return screening;
    }

    @Override
    @Transactional
    public ScreeningResponseDto saveScreening(final ScreeningCreationRequestDto dto) {
        FilmDto film = filmClient.findFilmById(dto.filmId());
        HallDto hall = hallClient.findHallById(dto.hallId());

        if (!hall.isActive()) {
            throw new CustomException(
                    this.messageSource.getMessage(
                            "screening.service.hall.is.inactive",
                            new Object[]{hall.id()},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        if (!screeningTimeFrameValidityCheck(
                dto.date(),
                dto.time(),
                film,
                hall
        )) {
            throw new CustomException(
                    this.messageSource.getMessage(
                            "screening.service.screening.invalid.time.frame",
                            new Object[]{dto.date().toString()},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        Screening screening = screeningRepository.save(screeningMapper.dtoToEntity(dto));
        screening.setSeats(createSeatsForScreening(
                screening.getId(),
                hall
        ));

        log.info("Saved screening with id {}", screening.getId());

        return screeningToDto.apply(screening);
    }

    @Override
    @Transactional
    public void deleteScreening(
            final Integer id
    ) {
        screeningRepository.deleteById(id);
        log.info("Deleted screening with id {}", id);
    }

    private List<Seat> createSeatsForScreening(
            Integer screeningId,
            HallDto hall
    ) {
        List<Seat> seats = new ArrayList<>();

        int rowCount = hall.rowCount();
        int seatInRowCount = hall.seatInRowCount();

        for (int i = 1; i <= rowCount; ++i) {
            for (int j = 1; j <= seatInRowCount; ++j) {
                seats.add(seatRepository.save(new Seat(
                        -1,
                        i,
                        j,
                        SeatStatus.AVAILABLE,
                        screeningId
                )));
            }
        }

        return seats;
    }

    private boolean screeningTimeFrameValidityCheck(
            LocalDate date,
            LocalTime time,
            FilmDto film,
            HallDto hall
    ) {
        List<ScreeningResponseDto> screeningsByHallIdAndDate = screeningRepository
                .findAllByHallIdAndDate(hall.id(), date)
                .stream()
                .map(screeningToDto)
                .toList();

        if (screeningsByHallIdAndDate.isEmpty()) {
            return true;
        }

        ScreeningResponseDto currentScreening;
        LocalTime currentScreeningStartTime;
        LocalTime currentScreeningEndTime;

        LocalTime screeningEndTime = time.plusMinutes((long) film.durationInMinutes());

        boolean mainTimeIntervalReversed = false;
        boolean currentTimeIntervalReversed = false;

        if (time.plusMinutes((long) film.durationInMinutes()).isBefore(time)) {
            mainTimeIntervalReversed = true;
        }

        for (ScreeningResponseDto screeningResponseDto : screeningsByHallIdAndDate) {
            currentScreening = screeningResponseDto;
            currentScreeningStartTime = currentScreening.time();
            currentScreeningEndTime = currentScreeningStartTime.plusMinutes(
                    (long) currentScreening.filmDto().durationInMinutes()
            );

            if (currentScreeningEndTime.isBefore(currentScreeningStartTime)) {
                currentTimeIntervalReversed = true;
            }

            if (!mainTimeIntervalReversed && !currentTimeIntervalReversed) {
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

    private final Function<Screening, ScreeningResponseDto> screeningToDto = new Function<>() {
        @Override
        public ScreeningResponseDto apply(Screening screening) {
            return new ScreeningResponseDto(
                    screening.getId(),
                    screening.getDate(),
                    screening.getTime(),
                    filmClient.findFilmById(screening.getFilmId()),
                    hallClient.findHallById(screening.getHallId()),
                    screening.getSeats().stream()
                            .map(seatMapper::entityToDto)
                            .toList()
            );
        }
    };
}
