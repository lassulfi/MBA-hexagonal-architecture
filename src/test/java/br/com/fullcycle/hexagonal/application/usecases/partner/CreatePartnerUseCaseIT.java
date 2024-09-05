package br.com.fullcycle.hexagonal.application.usecases.partner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.hexagonal.IntegrationTest;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

public class CreatePartnerUseCaseIT extends IntegrationTest {

    @Autowired
    private CreatePartnerUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @BeforeEach
    void setup() {
        this.partnerRepository.deleteAll();
    }

    @Test
    @DisplayName("Deve criar um parceiro")
    public void testCreatePartner() {
        // given
        final var expectedCNPJ = "55.918.450/0001-50";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";

        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, expectedEmail, expectedName);
        // when

        final var output = useCase.execute(createInput);

        // then

        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedCNPJ, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com CNPJ duplicado")
    public void testCreateWithDuplicatedCPFShouldFail() throws Exception {
        // given
        final var expectedCNPJ = "55.918.450/0001-50";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        createPartner(expectedCNPJ, expectedEmail, expectedName);
        
        final var createInput = new CreatePartnerUseCase.Input(expectedCNPJ, "j@j.com", expectedName);

        // when

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then

        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve cadastrar um parceiro com e-mail duplicado")
    public void testCreateWithDuplicatedEmailShouldFail() throws Exception {
        // given
        final var expectedCNPJ = "55.918.450/0001-50";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        final var expectedError = "Partner already exists";

        createPartner(expectedCNPJ, expectedEmail, expectedName);

        final var createInput = new CreatePartnerUseCase.Input("66.918.450/0001-50", expectedEmail, expectedName);        
        // when

        final var actualException = Assertions.assertThrows(ValidationException.class, () -> useCase.execute(createInput));

        // then

        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Partner createPartner(final String aCNPJ, final String anEmail, final String aName) {
        return this.partnerRepository.create(Partner.newPartner(aName, aCNPJ, anEmail));
    }
    
}
