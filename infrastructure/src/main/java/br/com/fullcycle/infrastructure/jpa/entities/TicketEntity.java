package br.com.fullcycle.infrastructure.jpa.entities;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.event.ticket.Ticket;
import br.com.fullcycle.domain.event.ticket.TicketId;
import br.com.fullcycle.domain.event.ticket.TicketStatus;

@Entity(name = "Ticket")
@Table(name = "tickets")
public class TicketEntity {

    @Id
    private UUID id;

    private UUID customerId;

    private UUID eventId;

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private Instant paidAt;

    private Instant reservedAt;

    public TicketEntity() {
    }

    public TicketEntity(
        final UUID id,
        final UUID customerId,
        final UUID eventId,
        final TicketStatus status,
        final Instant paidAt,
        final Instant reservedAt
    ) {
        this.id = id;
        this.customerId = customerId;
        this.eventId = eventId;
        this.status = status;
        this.paidAt = paidAt;
        this.reservedAt = reservedAt;
    }

    public static TicketEntity of(final Ticket aTicket) {
        return new TicketEntity(UUID.fromString(aTicket.getTicketId().value()), 
        UUID.fromString(aTicket.getCustomerId().value()), 
        UUID.fromString(aTicket.getEventId().value()), 
        aTicket.getTicketStatus(), 
        aTicket.getPaidAt(), 
        aTicket.getReservedAt());
    }

    public Ticket toTicket() {
        return new Ticket(
                TicketId.with(this.id.toString()),
                CustomerId.with(this.customerId.toString()),
                EventId.with(this.eventId.toString()),
                this.status,
                this.paidAt,
                this.reservedAt);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public Instant getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(Instant paidAt) {
        this.paidAt = paidAt;
    }

    public Instant getReservedAt() {
        return reservedAt;
    }

    public void setReservedAt(Instant reservedAt) {
        this.reservedAt = reservedAt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
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
        TicketEntity other = (TicketEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
