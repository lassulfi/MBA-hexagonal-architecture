package br.com.fullcycle.hexagonal.application.domain.person;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class CnpjTest {
    
    @Test
    @DisplayName("Deve instanciar um CNPJ válido")
    public void testCreateCnpj() {
        // given
        final var expectedCNPJ = "55.918.450/0001-50";

        // when
        final var actualCnpj = new Cnpj(expectedCNPJ);
        // then

        Assertions.assertEquals(expectedCNPJ, actualCnpj.value());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"55918.450/0001-50"})
    @DisplayName("Não deve instanciar um CNPJ invalido")
    public void testCreateCnpjWithInvalidCNPJ(String cnpj) {
        // given
        final var expectedMessage = "Invalid value for CNPJ";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> new Cnpj(cnpj));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
