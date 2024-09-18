package br.com.fullcycle.application.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.event.ticket.TicketRepository;

public class InMemoryTicketRepository implements TicketRepository {

    private final Map<String, Ticket> tickets;

    public InMemoryTicketRepository() {
        this.tickets = new HashMap<>();
    }

    @Override
    public Optional<Ticket> ticketOfId(TicketId anId) {
        return Optional.ofNullable(this.tickets.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Ticket create(Ticket aTicket) {
        this.tickets.put(aTicket.getTicketId().value().toString(), aTicket);
        return aTicket;
    }

    @Override
    public Ticket update(Ticket aTicket) {
        this.tickets.put(aTicket.getTicketId().value().toString(), aTicket);

        return aTicket;
    }

    @Override
    public void deleteAll() {
        this.tickets.clear();
    }
}