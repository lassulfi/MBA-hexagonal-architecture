package br.com.fullcycle.domain.event;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.exceptions.ValidationException;

// this class is used to define the direct
// relationship between two entities
// in this case the eventual consistency is not considered
public class EventTicket {
    
    private final EventTicketId eventTicketId;

    private final EventId eventId;
    
    private final CustomerId customerId;
    
    private TicketId ticketId;

    private int ordering;

    public EventTicket(
        final EventTicketId eventTicketId,
        final EventId eventId, 
        final CustomerId customerId, 
        final TicketId ticketId, 
        final Integer ordering
    ) {
        if (eventTicketId == null) {
            throw new ValidationException("Invalid ticketId for EventTicket");
        }

        if (eventId == null) {
            throw new ValidationException("Invalid eventId for EventTicket");
        }

        if (customerId == null) {
            throw new ValidationException("Invalid customerId for EventTicket");
        }

        this.eventTicketId = eventTicketId;
        this.eventId = eventId;
        this.customerId = customerId;
        this.ticketId = ticketId;
        this.setOrdering(ordering);
    }

    public static EventTicket newTicket(final EventId eventId, final CustomerId customerId, final int ordering) {
        return new EventTicket(EventTicketId.unique(), eventId, customerId, null, ordering);
    }

    public EventTicket associateTicket(final TicketId aTicket) {
        this.ticketId = aTicket;
        return this;
    }

    public EventTicketId getEventTicketId() {
        return eventTicketId;
    }

    public TicketId getTicketId() {
        return ticketId;
    }

    public EventId getEventId() {
        return eventId;
    }

    public int getOrdering() {
        return ordering;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }
    

    private void setOrdering(final Integer ordering) {
        if (ordering == null) {
            throw new ValidationException("Invalid ordering for EventTicket");
        }

        this.ordering = ordering;
    }
}
