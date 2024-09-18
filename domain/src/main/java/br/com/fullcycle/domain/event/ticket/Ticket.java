package br.com.fullcycle.domain.event.ticket;

import java.time.Instant;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

import br.com.fullcycle.domain.DomainEvent;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.EventTicketId;
import br.com.fullcycle.domain.exceptions.ValidationException;

public class Ticket {
    
    private final TicketId ticketId;

    private final Set<DomainEvent> domainEvents;

    private CustomerId customerId;

    private EventId eventId;

    private TicketStatus ticketStatus;

    private Instant paidAt;

    private Instant reservedAt;

    public Ticket(
        final TicketId ticketId, 
        final CustomerId customerId, 
        final EventId eventId, 
        final TicketStatus ticketStatus, 
        final Instant paidAt,
        final Instant reservedAt
    ) {
        this(ticketId);
        this.setCustomerId(customerId);
        this.setEventId(eventId);
        this.setTicketStatus(ticketStatus);
        this.setPaidAt(paidAt);
        this.setReservedAt(reservedAt);
    }

    public static Ticket newTicket(final CustomerId customerId, final EventId eventId) {
        return new Ticket(
            TicketId.unique(),
            customerId,
            eventId,
            TicketStatus.PENDING,
            null,
            Instant.now()
        );
    }

    public static Ticket newTicket(final EventTicketId eventTicketId, final CustomerId customerId, final EventId eventId) {
        final var aTicket = newTicket(customerId, eventId);
        aTicket.allDomainEvents().add(new TicketCreated(aTicket.getTicketId(), eventTicketId, eventId, customerId));

        return aTicket;
    }

    private Ticket(final TicketId ticketId) {
        if (ticketId == null) {
            throw new ValidationException("Invalid ticketId for Ticket");
        }

        this.ticketId = ticketId;

        this.domainEvents = new HashSet<>(2);
    }

    public TicketId getTicketId() {
        return ticketId;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public EventId getEventId() {
        return eventId;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public Set<DomainEvent> allDomainEvents() {
        return Collections.unmodifiableSet(this.domainEvents);
    }

    private void setCustomerId(final CustomerId customerId) {
        if (customerId == null) {
            throw new ValidationException("Invalid customerId for Ticket");
        }
        this.customerId = customerId;
    }

    private void setEventId(final EventId eventId) {
        if (eventId == null) {
            throw new ValidationException("Invalid eventId for Ticket");
        }
        this.eventId = eventId;
    }

    private void setTicketStatus(TicketStatus ticketStatus) {
        if (ticketStatus == null) {
            throw new ValidationException("Invalid ticketStatus for Ticket");
        }

        this.ticketStatus = ticketStatus;
    }

    private void setPaidAt(Instant paidAt) {   
        this.paidAt = paidAt;
    }

    private void setReservedAt(Instant reservedAt) {
        if (reservedAt == null) {
            throw new ValidationException("Invalid reservedAt for Ticket");
        }

        this.reservedAt = reservedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ticketId == null) ? 0 : ticketId.hashCode());
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
        Ticket other = (Ticket) obj;
        if (ticketId == null) {
            if (other.ticketId != null)
                return false;
        } else if (!ticketId.equals(other.ticketId))
            return false;
        return true;
    }
}
