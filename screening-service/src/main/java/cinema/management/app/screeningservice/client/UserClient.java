package cinema.management.app.screeningservice.client;

import cinema.management.app.screeningservice.dto.response.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service", url = "${application.config.user-url}")
public interface UserClient {

    @GetMapping
    UserDto getCurrentUserInfo(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String token
    );

}

