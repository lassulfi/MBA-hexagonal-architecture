package br.com.fullcycle.hexagonal.application.domain.customer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CustomerTest {

    @Test
    @DisplayName("Deve instanciar um cliente")
    public void testCreateCustomer() {
        // given
        final var expectedCPF = "811.104.200-05";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        // when
        final var actualCustomer = Customer.newCustomer(expectedName, expectedCPF, expectedEmail);

        // then

        Assertions.assertNotNull(actualCustomer.getCustomerId());
        Assertions.assertEquals(expectedCPF, actualCustomer.getCpf().value());
        Assertions.assertEquals(expectedEmail, actualCustomer.getEmail().value());
        Assertions.assertEquals(expectedName, actualCustomer.getName().value());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com um CPF invalido")
    public void testCreateCustomerWithInvalidCpf() {
        // given
        final var expectedMessage = "Invalid value for CPF";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Customer.newCustomer("John Doe", "811104.200-05", "john.doe@gmail.com"));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um cliente com um nome inválido")
    public void testCreateCustomerWithInvalidName() {
        // given
        final var expectedMessage = "Invalid value for name";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Customer.newCustomer(null, "811.104.200-05", "john.doe@gmail.com"));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "invalidemail" })
    @DisplayName("Não deve instanciar um cliente com um email invalido")
    public void testCreateCustomerWithInvalidEmail(String email) {
        // given
        final var expectedMessage = "Invalid value for email";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Customer.newCustomer("John Doe", "811.104.200-05", email));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
