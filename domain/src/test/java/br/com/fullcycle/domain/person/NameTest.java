package br.com.fullcycle.domain.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import br.com.fullcycle.domain.exceptions.ValidationException;

public class NameTest {
    
    @Test
    @DisplayName("Deve instanciar um nome válido")
    public void testCreateName() {
        // given
        final var expectedName = "John Doe";

        // when
        final var actualName = new Name(expectedName);
        // then

        Assertions.assertEquals(expectedName, actualName.value());
    }

    @Test
    @DisplayName("Não deve instanciar um nome invalido")
    public void testCreateNameWithInvalidName() {
        // given
        final var expectedMessage = "Invalid value for name";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> new Name(null));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
