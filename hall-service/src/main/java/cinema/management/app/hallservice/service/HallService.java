package cinema.management.app.hallservice.service;

import cinema.management.app.hallservice.dto.request.HallCreateRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateActivityStatusRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateRequestDto;
import cinema.management.app.hallservice.dto.response.HallResponseDto;

import java.util.List;

public interface HallService {

    List<HallResponseDto> findAllHalls();

    List<HallResponseDto> findAllHallsWithCurrentActivityStatus(final Boolean activityStatus);

    HallResponseDto findHallById(final Integer id);

    HallResponseDto saveHall(final HallCreateRequestDto dto);

    HallResponseDto updateHall(final Integer id, final HallUpdateRequestDto dto);

    HallResponseDto updateHallActivityStatus(final Integer id, final HallUpdateActivityStatusRequestDto dto);

    void deleteHall(final Integer id);

}
