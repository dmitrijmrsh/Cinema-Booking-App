package cinema.management.app.ticketsservice.repository;

import cinema.management.app.ticketsservice.entity.Ticket;

import java.util.List;

public interface TicketRepository {

    List<Ticket> findAllByUserId(final Integer id);

    Ticket save(final Ticket ticket);

    void deleteById(final Integer id);

}
