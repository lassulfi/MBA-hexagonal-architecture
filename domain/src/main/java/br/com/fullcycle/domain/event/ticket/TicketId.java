package br.com.fullcycle.domain.event.ticket;

import java.util.UUID;

import br.com.fullcycle.domain.exceptions.ValidationException;

public record TicketId(String value) {

    public TicketId {
        if (value == null) {
            throw new ValidationException("Invalid value for TicketId");
        }
    }

    public static TicketId unique() {
        return new TicketId(UUID.randomUUID().toString());
    }

    public static TicketId with(final String aValue) {
        try {
            return new TicketId(UUID.fromString(aValue).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for TicketId");
        }
    }
}
