package br.com.fullcycle.infrastructure.repositories;

import java.util.Optional;
import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.event.ticket.TicketRepository;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.entities.TicketEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.TicketJpaRepository;

@Component
public class TicketDatabaseRepository implements TicketRepository {

    private final TicketJpaRepository ticketJpaRepository;
    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper mapper;

    public TicketDatabaseRepository(
        final TicketJpaRepository ticketJpaRepository,
        final OutboxJpaRepository outboxJpaRepository,
        final ObjectMapper mapper
    ) {
        this.ticketJpaRepository = Objects.requireNonNull(ticketJpaRepository);
        this.outboxJpaRepository = Objects.requireNonNull(outboxJpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public Optional<Ticket> ticketOfId(final TicketId anId) {
        Objects.requireNonNull(anId, "id cannot be null");
        return this.ticketJpaRepository.findById(UUID.fromString(anId.value())).map(it -> it.toTicket());
    }

    @Override
    @Transactional
    public Ticket create(final Ticket aTicket) {
        this.outboxJpaRepository.saveAll(
            aTicket.allDomainEvents().stream()
            .map(it -> OutboxEntity.of(it, this::toJson))
            .toList()
        );

        return this.ticketJpaRepository.save(TicketEntity.of(aTicket)).toTicket();
    }

    @Override
    @Transactional
    public Ticket update(final Ticket aTicket) {
        this.outboxJpaRepository.saveAll(
            aTicket.allDomainEvents().stream()
            .map(it -> OutboxEntity.of(it, this::toJson))
            .toList()
        );

        return this.ticketJpaRepository.save(TicketEntity.of(aTicket)).toTicket();
    }

    @Override
    public void deleteAll() {
        this.ticketJpaRepository.deleteAll();
    }

    private String toJson(final DomainEvent anEvent) {
        try {
            return this.mapper.writeValueAsString(anEvent);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
}
