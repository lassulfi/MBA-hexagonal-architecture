package br.com.fullcycle.hexagonal.application.domain.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CpfTest {
    
    @Test
    @DisplayName("Deve instanciar um Cpf válido")
    public void testCreateCpf() {
        // given
        final var expectedCpf = "811.104.200-05";

        // when
        final var actualCpf = new Cpf(expectedCpf);
        // then

        Assertions.assertEquals(expectedCpf, actualCpf.value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"55918.450/0001-50"})
    @DisplayName("Não deve instanciar um Cpf invalido")
    public void testCreateCpfWithInvalidCpf(String cpf) {
        // given
        final var expectedMessage = "Invalid value for CPF";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> new Cpf(cpf));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
