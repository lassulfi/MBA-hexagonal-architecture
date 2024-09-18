package br.com.fullcycle.application.partner;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.application.repository.InMemoryPartnerRepository;
import br.com.fullcycle.domain.partner.Partner;
import java.util.UUID;

public class GetPartnerByIdUseCaseTest {

    @Test
    @DisplayName("Deve obter um parceiro por id")
    public void testGetById() {
        // given
        final var expectedCNPJ = "55.918.450/0001-50";
        final var expectedEmail = "john.doe@gmail.com";
        final var expectedName = "John Doe";
        
        final var aPartner = Partner.newPartner(expectedName, expectedCNPJ, expectedEmail);
        final var expectedId = aPartner.getPartnerId().value().toString();

        final var partnerRepository = new InMemoryPartnerRepository();
        partnerRepository.create(aPartner);


        final var input = new GetPartnerByIdUseCase.Input(expectedId);
        // when

        final var useCase = new GetPartnerByIdUseCase(partnerRepository);
        final var output = useCase.execute(input).get();

        // then
        Assertions.assertEquals(expectedId, output.id());
        Assertions.assertEquals(expectedCNPJ, output.cnpj());
        Assertions.assertEquals(expectedEmail, output.email());
        Assertions.assertEquals(expectedName, output.name());
    }

    @Test
    @DisplayName("Deve obter vazio ao tentar recuperar um parceiro não existente por id")
    public void testGetByIdWithInvalidId() {
        // given
        final var expectedId = UUID.randomUUID().toString();

        final var partnerRepository = new InMemoryPartnerRepository();

        final var input = new GetPartnerByIdUseCase.Input(expectedId);

        // when

        final var useCase = new GetPartnerByIdUseCase(partnerRepository);
        final var output = useCase.execute(input);

        // then
        Assertions.assertTrue(output.isEmpty());
    }
}
