package br.com.fullcycle.domain.event.ticket;

import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> ticketOfId(TicketId anId);

    Ticket create(Ticket aTicket);

    Ticket update(Ticket aTicket);

    void deleteAll();
}
