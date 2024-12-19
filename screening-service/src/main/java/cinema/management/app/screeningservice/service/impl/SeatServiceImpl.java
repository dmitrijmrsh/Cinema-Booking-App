package cinema.management.app.screeningservice.service.impl;

import cinema.management.app.screeningservice.client.TicketsClient;
import cinema.management.app.screeningservice.client.UserClient;
import cinema.management.app.screeningservice.constant.SeatStatus;
import cinema.management.app.screeningservice.dto.request.SeatReserveRequestDto;
import cinema.management.app.screeningservice.dto.request.TicketCreationRequestDto;
import cinema.management.app.screeningservice.dto.request.TicketExistenceCheckRequestDto;
import cinema.management.app.screeningservice.dto.response.SeatResponseDto;
import cinema.management.app.screeningservice.entity.Screening;
import cinema.management.app.screeningservice.entity.Seat;
import cinema.management.app.screeningservice.exception.CustomException;
import cinema.management.app.screeningservice.mapper.SeatMapper;
import cinema.management.app.screeningservice.repository.ScreeningRepository;
import cinema.management.app.screeningservice.repository.SeatRepository;
import cinema.management.app.screeningservice.service.SeatService;
import cinema.management.app.screeningservice.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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

@Service
@RequiredArgsConstructor
@Log4j2
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final ScreeningRepository screeningRepository;
    private final UserClient userClient;
    private final TicketsClient ticketsClient;
    private final SeatMapper seatMapper;
    private final MessageSource messageSource;

    @Override
    public List<SeatResponseDto> findAvailableSeatsOnScreening(final Integer screeningId) {
        List<SeatResponseDto> availableSeats = seatRepository.findAvailableByScreeningId(screeningId).stream()
                .map(seatMapper::entityToDto)
                .toList();

        log.info("Found {} seats with screening id {}", availableSeats.size(), screeningId);

        return availableSeats;
    }

    @Override
    public SeatResponseDto findById(final Integer id) {
        SeatResponseDto seat = seatRepository.findById(id)
                .map(seatMapper::entityToDto)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "screening.service.not.found.seat.by.id",
                                new Object[]{id},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));

        log.info("Found seat with id {}", id);

        return seat;
    }

    @Override
    @Transactional
    public SeatResponseDto reserveSeat(
            final Integer screeningId,
            final SeatReserveRequestDto dto,
            HttpServletRequest httpRequest
    ) {
        Integer rowNumber = dto.rowNumber();
        Integer seatInRow = dto.seatInRow();
        Integer currentUserId = userClient.getCurrentUserInfo(JwtUtil.getTokenFromHttpRequest(
                httpRequest
        )).id();

        Seat seat = seatRepository.findByScreeningIdAndRowNumberAndSeatInRow(
                screeningId,
                rowNumber,
                seatInRow
        ).orElseThrow(() -> new CustomException(
                this.messageSource.getMessage(
                        "screening.service.not.found.seat.in.screening.by.rows.number.and.seat.in.row",
                        new Object[]{screeningId, rowNumber, seatInRow},
                        LocaleContextHolder.getLocale()
                ),
                HttpStatus.NOT_FOUND
        ));

        validateReserveSeatRequest(screeningId,currentUserId);

        if (seat.getStatus() == SeatStatus.AVAILABLE) {
            seat = seatRepository.updateStatus(
                    seat.getId(),
                    SeatStatus.TAKEN
            );

            log.info(
                    "Reserved seat with {} row and {} seat in row number on screening with id {}",
                    rowNumber,
                    seatInRow,
                    screeningId
            );

            ticketsClient.createTicket(new TicketCreationRequestDto(
                    currentUserId, screeningId, seat.getId()
            ));

            return seatMapper.entityToDto(seat);
        }

        log.error(
                "Seat with {} row and {} seat in row number on screening with id {} is already reserved",
                rowNumber,
                seatInRow,
                screeningId
        );

        throw new CustomException(
                this.messageSource.getMessage(
                        "screening.service.seat.is.already.reserved",
                        new Object[]{screeningId, rowNumber, seatInRow},
                        LocaleContextHolder.getLocale()
                ),
                HttpStatus.UNPROCESSABLE_ENTITY
        );
    }

    private void validateReserveSeatRequest(
            final Integer screeningId,
            final Integer userId
    ) {
        Screening screening = screeningRepository.findById(screeningId)
                .orElseThrow(() -> new CustomException(
                        this.messageSource.getMessage(
                                "screening.service.not.found.screening.by.id",
                                new Object[]{screeningId},
                                LocaleContextHolder.getLocale()
                        ),
                        HttpStatus.NOT_FOUND
                ));

        if (screening.getDate().isBefore(LocalDate.now()) ||
                (screening.getDate().equals(LocalDate.now()) && screening.getTime().isBefore(LocalTime.now()))) {
            throw new CustomException(
                    this.messageSource.getMessage(
                            "screening.service.screening.passed",
                            new Object[]{screeningId},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
            );
        }

        if (ticketsClient.checkTicketExistsByUserIdAndScreeningId(userId, screeningId)) {
            System.out.println("DAAAAAAAA");
            throw new CustomException(
                    this.messageSource.getMessage(
                            "screening.service.user.already.reserved.seat",
                            new Object[]{userId, screeningId},
                            LocaleContextHolder.getLocale()
                    ),
                    HttpStatus.CONFLICT
            );
        }
    }
}
