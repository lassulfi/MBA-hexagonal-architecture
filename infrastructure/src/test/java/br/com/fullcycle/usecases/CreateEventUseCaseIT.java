package br.com.fullcycle.usecases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import br.com.fullcycle.IntegrationTest;
import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.partner.PartnerRepository;
import jakarta.transaction.Transactional;

public class CreateEventUseCaseIT extends IntegrationTest {

    @Autowired
    private CreateEventUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    void setup() {
        partnerRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Deve criar um evento")
    public void testCreate() throws Exception {
        // given
        final var expectedDate = "2021-01-01";
        final var expectedName = "Disney on Ice";
        final var expectedTotalSpots = 100;
        
        final var aPartner = createPartner("55.918.450/0001-50", "john.doe@gmail.com", "John Doe");
        
        final var expectedPartnerId = aPartner.getPartnerId().value();
        
        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId,
                expectedTotalSpots);

        // when
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

        final var createInput = new CreateEventUseCase.Input(expectedDate, expectedName, expectedPartnerId,
                expectedTotalSpots);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(createInput));

        // then
        Assertions.assertEquals(expectedError, actualException.getMessage());
    }

    private Partner createPartner(final String aCnpj, final String anEmail, final String aName) {
        return partnerRepository.create(Partner.newPartner(aName, aCnpj, anEmail));
    }
}
