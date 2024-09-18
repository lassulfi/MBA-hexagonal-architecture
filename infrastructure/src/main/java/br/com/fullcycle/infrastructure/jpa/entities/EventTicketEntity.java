package br.com.fullcycle.infrastructure.jpa.entities;

import java.util.UUID;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicket;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.event.ticket.TicketId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "EventTicket")
@Table(name = "events_tickets")
public class EventTicketEntity {
    
    @Id
    private UUID eventTicketId;

    private UUID customerId;
    
    private int ordering;

    private UUID ticketId;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventEntity event;

    public EventTicketEntity() {
    }

    public EventTicketEntity(
        final UUID eventTicketId,
        final UUID customerId, 
        final int ordering,
        final UUID ticketId, 
        final EventEntity event
    ) {
        this.eventTicketId = eventTicketId;
        this.customerId = customerId;
        this.ordering = ordering;
        this.ticketId = ticketId;
        this.event = event;
    }

    public static EventTicketEntity of(final EventEntity event, final EventTicket eventTicket) {
        return new EventTicketEntity(
            UUID.fromString(eventTicket.getEventTicketId().value()),
            UUID.fromString(eventTicket.getCustomerId().value()),
            eventTicket.getOrdering(),
            eventTicket.getTicketId() != null ? UUID.fromString(eventTicket.getTicketId().value()) : null,
            event
        );
    }

    public EventTicket toEventTicket() {
        return new EventTicket(
            EventTicketId.with(eventTicketId.toString()),
            EventId.with(event.getId().toString()),
            CustomerId.with(customerId.toString()), 
            this.ticketId != null ? TicketId.with(ticketId.toString()) : null, 
            ordering);
    }

    public UUID getTicketId() {
        return ticketId;
    }

    public void setTicketId(UUID ticketId) {
        this.ticketId = ticketId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public int getOrdering() {
        return ordering;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public EventEntity getEvent() {
        return event;
    }

    public void setEvent(EventEntity event) {
        this.event = event;
    }

    public UUID getEventTicketId() {
        return eventTicketId;
    }

    public void setEventTicketId(UUID eventTicketId) {
        this.eventTicketId = eventTicketId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((eventTicketId == null) ? 0 : eventTicketId.hashCode());
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
        EventTicketEntity other = (EventTicketEntity) obj;
        if (eventTicketId == null) {
            if (other.eventTicketId != null)
                return false;
        } else if (!eventTicketId.equals(other.eventTicketId))
            return false;
        return true;
    }
}
