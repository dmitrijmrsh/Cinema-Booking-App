package cinema.management.app.hallservice.service;

import cinema.management.app.hallservice.dto.request.HallCreateRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateActivityStatusRequestDto;
import cinema.management.app.hallservice.dto.request.HallUpdateRequestDto;
import cinema.management.app.hallservice.dto.response.HallResponseDto;

import java.util.List;

public interface HallService {

    List<HallResponseDto> findAllHalls();

    List<HallResponseDto> findAllHallsWithCurrentActivityStatus(Boolean activityStatus);

    HallResponseDto findHallById(Integer id);

    HallResponseDto saveHall(HallCreateRequestDto dto);

    HallResponseDto updateHall(Integer id, HallUpdateRequestDto dto);

    HallResponseDto updateHallActivityStatus(Integer id, HallUpdateActivityStatusRequestDto dto);

    void deleteHall(Integer id);

}
