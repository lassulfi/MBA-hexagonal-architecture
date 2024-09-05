package br.com.fullcycle.hexagonal.application.domain.partner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;

public class PartnerTest {

    @Test
    @DisplayName("Deve instanciar um parceiro")
    public void testCreatePartner() {
        // given
        final var expectedCNPJ = "55.918.450/0001-50";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        // when
        final var actualPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);

        // then

        Assertions.assertNotNull(actualPartner.getPartnerId());
        Assertions.assertEquals(expectedCNPJ, actualPartner.getCnpj().value());
        Assertions.assertEquals(expectedEmail, actualPartner.getEmail().value());
        Assertions.assertEquals(expectedName, actualPartner.getName().value());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com um CNPJ invalido")
    public void testCreatePartnerWithInvalidCNPJ() {
        // given
        final var expectedMessage = "Invalid value for CNPJ";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Partner.newPartner("John Doe",  "55918.450/0001-50", "john.doe@gmail.com"));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve instanciar um parceiro com um nome inválido")
    public void testCreatePartnerWithInvalidName() {
        // given
        final var expectedMessage = "Invalid value for name";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Partner.newPartner(null, "55.918.450/0001-50", "john.doe@gmail.com"));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = { "invalidemail" })
    @DisplayName("Não deve instanciar um parceiro com um email invalido")
    public void testCreatePartnerWithInvalidEmail(String email) {
        // given
        final var expectedMessage = "Invalid value for email";

        // when
        final var actualException = Assertions.assertThrows(
            ValidationException.class, 
            () -> Partner.newPartner("John Doe", "55.918.450/0001-50", email));
        // then

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
