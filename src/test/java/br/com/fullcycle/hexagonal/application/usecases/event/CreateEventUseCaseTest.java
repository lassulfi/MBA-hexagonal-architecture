package br.com.fullcycle.hexagonal.application.usecases.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.InMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryPartnerRepository;

public class CreateEventUseCaseTest {

    @Test
    @DisplayName("Deve criar um evento")
    public void testCreate() throws Exception {
        // given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        
        final var partnerRepository = new InMemoryPartnerRepository();
        final var eventRepository = new InMemoryEventRepository();
        
        final var aPartner = partnerRepository.create(Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com"));
        final var expectedPartnerId = aPartner.getPartnerId().value();

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId,
                expectedTotalSpots);

        // when

        final var useCase = new CreateEventUseCase(partnerRepository, eventRepository);

        final var output = useCase.execute(createInput);

        // then
        Assertions.assertNotNull(output.id());
        Assertions.assertEquals(expectedName, output.name());
        Assertions.assertEquals(expectedDate, output.date());
        Assertions.assertEquals(expectedPartnerId, output.partnerId());
        Assertions.assertEquals(expectedTotalSpots, output.totalSpots());
    }

    @Test
    @DisplayName("Não deve criar um evento quando um partner não for encontrado")
    public void testCreateEventWhenPartnerDoesNotExists_shouldThrowError() throws Exception {
        // given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        final var expectedPartnerId = PartnerId.unique().value();
        final var expectedError = "Partner not found";

        final var partnerRepository = new InMemoryPartnerRepository();
        final var eventRepository = new InMemoryEventRepository();

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId,
                expectedTotalSpots);

        // when
        final var useCase = new CreateEventUseCase(partnerRepository, eventRepository);

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }
}
