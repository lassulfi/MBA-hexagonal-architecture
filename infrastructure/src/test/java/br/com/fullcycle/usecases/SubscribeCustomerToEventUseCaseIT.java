package br.com.fullcycle.usecases;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import br.com.fullcycle.IntegrationTest;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.domain.customer.Customer;
import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.Event;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.partner.PartnerRepository;
import jakarta.transaction.Transactional;

public class SubscribeCustomerToEventUseCaseIT extends IntegrationTest {

    @Autowired
    private SubscribeCustomerToEventUseCase useCase;

    @Autowired
    private PartnerRepository partnerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EventRepository eventRepository;

    @AfterEach
    void setup() {
        this.partnerRepository.deleteAll();
        this.customerRepository.deleteAll();
        this.eventRepository.deleteAll();
    }

    @Test
    @Transactional
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
        // given
        final var aPartner = createPartner("55.918.450/0001-50", "john.doe@email.com", "John Doe");

        final var anEvent = createAnEvent("Disney", "2024-01-01", 100, aPartner);

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");

        final var expectedEventId = anEvent.getEventId().value();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(expectedEventId, aCustomer.getCustomerId().value());

        // when
        final var output = useCase.execute(subscribeInput);
        // then

        Assertions.assertNotNull(output.eventTicketId());
        Assertions.assertEquals(expectedEventId, output.eventId());
        Assertions.assertNotNull(output.reservationDate());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWithoutEvent() throws Exception {
        // given
        final var expectedErrorMessage = "Event not found";

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");

        final var eventId = EventId.unique().value();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(eventId, aCustomer.getCustomerId().value());

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Não deve comprar um ticket para um cliente que não existe")
    public void testReserveTicketWithoutCustomer() throws Exception {
        // given
        final var expectedErrorMessage = "Customer not found";

        final var aPartner = createPartner("55.918.450/0001-50", "john.doe@email.com", "John Doe");

        final var customerId = CustomerId.unique().value();

        final var anEvent = createAnEvent("Disney", "2024-01-01", 100, aPartner);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), customerId);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Um mesmo cliente não pode comprar mais de um ticket por evento")
    public void testReserveTickerMoreThanOnce() throws Exception {
        // given
        final var expectedErrorMessage = "Email already subscribed";

        final var aPartner = createPartner("55.918.450/0001-50", "john.doe@email.com", "John Doe");

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");

        final var anEvent = createAnEvent("Disney", "2024-01-01", 100, aPartner);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), aCustomer.getCustomerId().value());

        useCase.execute(subscribeInput);

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @Transactional
    @DisplayName("Não deve comprar um ticket para um evento esgotado")
    public void testReserveTicketForSoldOutEventShouldFail() throws Exception {
        // given
        final var expectedErrorMessage = "Event sold out";

        final var aPartner = createPartner("55.918.450/0001-50", "john.doe@email.com", "John Doe");

        final var aCustomer = createCustomer("123.456.789-01", "john.doe@gmail.com", "John Doe");

        final var anEvent = createAnEvent("Disney", "2024-01-01", 0, aPartner);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), aCustomer.getCustomerId().value());

        // when
        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private Partner createPartner(final String aCNPJ, final String anEmail, final String aName) {
        return this.partnerRepository.create(Partner.newPartner(aName, aCNPJ, anEmail));
    }

    private Customer createCustomer(final String aCPF, final String anEmail, final String aName) {
        return this.customerRepository.create(Customer.newCustomer(aName, aCPF, anEmail));
    }

    private Event createAnEvent(final String aName, final String aDate, final int totalSpots, final Partner aPartner) {
        return this.eventRepository.create(Event.newEvent(aName, aDate, totalSpots, aPartner));
    }
}
