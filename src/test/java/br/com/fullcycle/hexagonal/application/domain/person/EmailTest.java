package br.com.fullcycle.hexagonal.application.domain.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class EmailTest {
    
    @Test
    @DisplayName("Deve instanciar um Email válido")
    public void testCreateEmail() {
        // given
        final var expectedEmail = "john@doe.com";

        // when
        final var actualEmail = new Email(expectedEmail);
        // then

        Assertions.assertEquals(expectedEmail, actualEmail.value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"invalidemail"})
    @DisplayName("Não deve instanciar um Email invalido")
    public void testCreateEmailWithInvalidEmail(String Email) {
        // given
        final var expectedMessage = "Invalid value for email";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> new Email(Email));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
