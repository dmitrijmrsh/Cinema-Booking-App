package cinema.management.app.screeningservice.service.impl;

import cinema.management.app.screeningservice.dto.request.SeatReserveRequestDto;
import cinema.management.app.screeningservice.dto.response.SeatResponseDto;
import cinema.management.app.screeningservice.entity.Seat;
import cinema.management.app.screeningservice.entity.enums.SeatStatus;
import cinema.management.app.screeningservice.exception.CustomException;
import cinema.management.app.screeningservice.mapper.SeatMapper;
import cinema.management.app.screeningservice.repository.SeatRepository;
import cinema.management.app.screeningservice.service.SeatService;
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
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatMapper seatMapper;
    private final MessageSource messageSource;

    @Override
    public List<SeatResponseDto> findAvailableSeatsOnScreening(Integer screeningId) {
        List<Seat> availableSeats = seatRepository.findAvailableSeatsByScreeningId(screeningId);

        log.info("Found {} seats with screening id {}", availableSeats.size(), screeningId);

        return availableSeats.stream()
                .map(seatMapper::entityToDto)
                .toList();
    }

    @Override
    @Transactional
    public SeatResponseDto reserveSeat(Integer screeningId, SeatReserveRequestDto dto) {
        Integer rowsNumber = dto.rowNumber();
        Integer seatInRow = dto.seatInRow();

        Seat seat =  seatRepository.findByScreeningIdAndRowNumberAndSeatInRow(
                screeningId, rowsNumber, seatInRow
        ).orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "screening.service.not.found.seat.in.screening.by.rows.number.and.seat.in.row",
                        new Object[]{screeningId, rowsNumber, seatInRow},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND)
        );

        if (seat.getStatus() == SeatStatus.AVAILABLE) {
            seat.setStatus(SeatStatus.TAKEN);
            seat = seatRepository.save(seat);

            log.info(
                    "Reserved seat with {} row and {} seat in row on screening with id {}",
                    rowsNumber,
                    seatInRow,
                    screeningId
            );

            return seatMapper.entityToDto(seat);
        }

        log.info(
                "Error : seat with {} row and {} seat in row on screening with id {} is already reserved",
                rowsNumber,
                seatInRow,
                screeningId
        );

        throw new CustomException(messageSource.getMessage(
                "screening.service.seat.is.already.reserved",
                new Object[]{screeningId, rowsNumber, seatInRow},
                LocaleContextHolder.getLocale()
        ), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
