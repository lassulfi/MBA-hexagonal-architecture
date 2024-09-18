package br.com.fullcycle.domain.event;

import java.util.Optional;

public interface EventRepository {
    Optional<Event> eventOfId(EventId anId);

    Event create(Event aEvent);

    Event update(Event aEvent);

    void deleteAll();
}
