package br.com.fullcycle.hexagonal.application.domain.person;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public record Email(String value) {
    public Email {
        if (value == null
                || !value.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new ValidationException("Invalid value for email");
        }
    }
}
