package br.com.fullcycle.domain.customer;

import java.util.UUID;

import br.com.fullcycle.domain.exceptions.ValidationException;

public record CustomerId(String value) {

    public CustomerId {
        if (value == null) {
            throw new ValidationException("Invalid value for CustomerId");
        }
    }

    public static CustomerId unique() {
        return new CustomerId(UUID.randomUUID().toString());
    }

    public static CustomerId with(final String aValue) {
        try {
            return new CustomerId(UUID.fromString(aValue).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for CustomerId");
        }
    }
}
