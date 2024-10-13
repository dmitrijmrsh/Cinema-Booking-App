package cinema.management.app.hallservice.service.impl;

import cinema.management.app.hallservice.dto.request.HallCreateRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateActivityStatusRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateRequestDto;
import cinema.management.app.hallservice.dto.response.HallResponseDto;
import cinema.management.app.hallservice.entity.Hall;
import cinema.management.app.hallservice.exception.CustomException;
import cinema.management.app.hallservice.mapper.HallMapper;
import cinema.management.app.hallservice.repository.HallRepository;
import cinema.management.app.hallservice.service.HallService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final MessageSource messageSource;

    @Override
    public List<HallResponseDto> findAllHalls() {
        List<HallResponseDto> halls = hallRepository.findAll().stream()
                .map(hallMapper::entityToDto)
                .toList();

        log.info("Found hall list with size {}", halls.size());

        return halls;
    }

    @Override
    public List<HallResponseDto> findAllHallsWithCurrentActivityStatus(Boolean activityStatus) {
        List<HallResponseDto> halls = hallRepository.findHallsByIsActive(activityStatus).stream()
                .map(hallMapper::entityToDto)
                .toList();

        log.info(
                "Found hall list with size {} and statuses {}",
                halls.size(),
                activityStatus ? "ACTIVE" : "INACTIVE"
        );

        return halls;
    }

    @Override
    public HallResponseDto findHallById(Integer id) {
        Hall hall = findById(id);

        log.info("Found hall with id {}", hall.getId());

        return hallMapper.entityToDto(hall);
    }

    @Override
    public HallResponseDto saveHall(HallCreateRequestDto dto) {
        Hall hall = hallRepository.save(hallMapper.dtoToEntity(dto));

        log.info("Saved hall with id {}", hall.getId());

        return hallMapper.entityToDto(hall);
    }

    @Override
    @Transactional
    public HallResponseDto updateHall(Integer id, HallUpdateRequestDto dto) {
        Hall hall = findById(id);

        hall.setIsActive(dto.isActive());
        hall.setRowCount(dto.rowCount());
        hall.setSeatInRowCount(dto.seatInRowCount());

        Hall updatedHall = hallRepository.save(hall);

        log.info("Updated hall with id {}", updatedHall.getId());

        return hallMapper.entityToDto(updatedHall);
    }

    @Override
    @Transactional
    public HallResponseDto updateHallActivityStatus(Integer id, HallUpdateActivityStatusRequestDto dto) {
        Hall hall = findById(id);

        hall.setIsActive(dto.isActive());

        Hall updatedHall = hallRepository.save(hall);

        log.info(
                "Updated activity status of hall with id {} for status {}",
                updatedHall.getId(),
                updatedHall.getIsActive() ? "ACTIVE" : "INACTIVE"
        );

        return hallMapper.entityToDto(updatedHall);
    }

    @Override
    @Transactional
    public void deleteHall(Integer id) {
        hallRepository.deleteById(id);

        log.info("Deleted hall with id {}", id);
    }

    private Hall findById(Integer id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new CustomException(messageSource.getMessage(
                        "hall.service.errors.not.found.hall.by.id",
                        new Object[] {id},
                        LocaleContextHolder.getLocale()
                ), HttpStatus.NOT_FOUND));
    }
}