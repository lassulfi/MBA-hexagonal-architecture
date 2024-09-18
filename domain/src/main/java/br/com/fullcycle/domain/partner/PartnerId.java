package br.com.fullcycle.domain.partner;

import java.util.UUID;

import br.com.fullcycle.domain.exceptions.ValidationException;

public record PartnerId(String value) {

    public PartnerId {
        if (value == null) {
            throw new ValidationException("Invalid value for PartnerId");
        }
    }

    public static PartnerId unique() {
        return new PartnerId(UUID.randomUUID().toString());
    }

    public static PartnerId with(final String aValue) {
        try {
            return new PartnerId(UUID.fromString(aValue).toString());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Invalid value for PartnerId");
        }
    }
}
