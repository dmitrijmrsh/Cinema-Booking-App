package cinema.management.app.webclient.client.tickets;

import cinema.management.app.webclient.dto.tickets.response.TicketResponseDto;

import java.util.List;

public interface TicketsRestClient {

    String TICKETS_BASE_URI = "http://localhost:8222/api/v1/tickets";
    String USER_BASE_URI = "http://localhost:8222/api/v1/user";

    List<TicketResponseDto> findAllTicketsForCurrentUser();

    void deleteAllExpiredTicketsForCurrentUser();

}
