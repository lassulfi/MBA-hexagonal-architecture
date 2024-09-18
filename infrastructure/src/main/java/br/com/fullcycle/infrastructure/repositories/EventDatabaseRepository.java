package br.com.fullcycle.infrastructure.repositories;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.infrastructure.jpa.entities.EventEntity;
import br.com.fullcycle.infrastructure.jpa.entities.OutboxEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.EventJpaRepository;
import br.com.fullcycle.infrastructure.jpa.repositories.OutboxJpaRepository;
import jakarta.transaction.Transactional;

@Component
public class EventDatabaseRepository implements EventRepository {

    private final EventJpaRepository eventJpaRepository;
    private final OutboxJpaRepository outboxJpaRepository;
    private final ObjectMapper mapper;

    public EventDatabaseRepository(
        final EventJpaRepository eventJpaRepository,
        final OutboxJpaRepository outboxJpaRepository,
        final ObjectMapper mapper) {
        this.eventJpaRepository = Objects.requireNonNull(eventJpaRepository);
        this.outboxJpaRepository = Objects.requireNonNull(outboxJpaRepository);
        this.mapper = Objects.requireNonNull(mapper);
    }

    @Override
    public Optional<Event> eventOfId(final EventId anId) {
        return this.eventJpaRepository.findById(UUID.fromString(anId.value())).map(it -> it.toEvent());
    }

    @Override
    @Transactional
    public Event create(final Event anEvent) {
        return save(anEvent);
    }

    @Override
    @Transactional
    public Event update(final Event anEvent) {
        return save(anEvent);
    }

    @Override
    public void deleteAll() {
        this.eventJpaRepository.deleteAll();
    }
    
    private Event save(final Event anEvent) {
        this.outboxJpaRepository.saveAll(
            anEvent.allDomainEvents().stream()
            .map(it -> OutboxEntity.of(it, this::toJson))
            .toList()
        );
        return this.eventJpaRepository.save(EventEntity.of(anEvent)).toEvent();
    }

    private String toJson(final DomainEvent anEvent) {
        try {
            return this.mapper.writeValueAsString(anEvent);
        } catch (final Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
