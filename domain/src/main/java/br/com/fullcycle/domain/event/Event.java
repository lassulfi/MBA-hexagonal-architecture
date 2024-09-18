package br.com.fullcycle.domain.event;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.Collections;
import java.util.Objects;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Name;
import br.com.fullcycle.domain.exceptions.ValidationException;
import java.util.HashSet;

public class Event {

    private static final int ONE = 1;

    private final EventId eventId;

    private final Set<EventTicket> tickets;

    private final Set<DomainEvent> domainEvents;

    private Name name;

    private LocalDate date;

    private int totalSpots;

    private PartnerId partnerId;

    public Event(
        final EventId eventId, 
        final String name, 
        final String date, 
        final Integer totalSpots, 
        final PartnerId partnerId,
        final Set<EventTicket> tickets
    ) {
        this(eventId, tickets);
        this.setName(name);
        this.setDate(date);
        this.setTotalSpots(totalSpots);
        this.setPartnerId(partnerId);
    }

    private Event(final EventId eventId, final Set<EventTicket> tickets) {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for Event");
        }

        this.eventId = eventId;

        this.tickets = tickets != null ? tickets : new HashSet<>(0);
        this.domainEvents = new HashSet<>(2);
    }

    public static Event newEvent(final String name, final String date, final Integer totalSpots, final Partner partner) {
        return new Event(EventId.unique(), name, date, totalSpots, partner.getPartnerId(), null);
    }

    public EventTicket reserveTicket(final CustomerId aCustomerId) {
        this.allTickets().stream()
            .filter(it -> Objects.equals(it.getCustomerId(), aCustomerId))
            .findFirst()
            .ifPresent(it -> {
                throw new ValidationException("Email already subscribed");
            });

        if (totalSpots < allTickets().size() + ONE) {
            throw new ValidationException("Event sold out");
        }

        final var aTicket = EventTicket.newTicket(this.getEventId(), aCustomerId, allTickets().size() + ONE);
        
        this.tickets.add(aTicket);
        this.domainEvents.add(new EventTicketReserved(aTicket.getEventTicketId() ,getEventId(), aCustomerId));
        
        return aTicket;
    }

    public EventId getEventId() {
        return eventId;
    }

    public Name getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getTotalSpots() {
        return totalSpots;
    }

    public PartnerId getPartnerId() {
        return partnerId;
    }

    public Set<EventTicket> allTickets() {
        return Collections.unmodifiableSet(this.tickets);
    }

    public Set<DomainEvent> allDomainEvents() {
        return Collections.unmodifiableSet(this.domainEvents);
    }

    // we can use setters to encapsulate validation logic
    // but we can also use constructor validation
    // we don't use public setters so we don't have an anemic entity

    private void setName(final String name) {
        this.name = new Name(name);
    }

    private void setDate(final String date) {
        if (date == null) {
            throw new ValidationException("Invalid date for Event");
        }

        try {
            this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (RuntimeException e) {
            throw new ValidationException("Invalid date for Event");
        }
    }

    private void setPartnerId(final PartnerId partnerId) {
        if (partnerId == null) {
            throw new ValidationException("Invalid partnerId for Event");
        }

        this.partnerId = partnerId;
    }

    private void setTotalSpots(final Integer totalSpots) {
        if (totalSpots == null) {
            throw new ValidationException("Invalid totalSpots for Event");
        }

        this.totalSpots = totalSpots;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventId == null) ? 0 : eventId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        return true;
    }

    public static Event restore(
        final String id, 
        final String name,
        final String date, 
        final int totalSpots,
        final String partnerId,
        final Set<EventTicket> eventTickets
    ) {
        return new Event(
            EventId.with(id), 
            name, 
            date, 
            totalSpots, 
            PartnerId.with(partnerId),
            eventTickets);
    }
}
