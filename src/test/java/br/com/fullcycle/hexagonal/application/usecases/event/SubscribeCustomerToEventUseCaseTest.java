package br.com.fullcycle.hexagonal.application.usecases.event;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.domain.event.ticket.TicketStatus;
import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repository.InMemoryCustomerRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryEventRepository;
import br.com.fullcycle.hexagonal.application.repository.InMemoryTicketRepository;

public class SubscribeCustomerToEventUseCaseTest {

    @Test
    @DisplayName("Deve comprar um ticket de um evento")
    public void testReserveTicket() throws Exception {
        // given
        final var expectedTicketsSize = 1;

        final var aCustomer = Customer.newCustomer("John Doe", "811.104.200-05", "john.doe@gmail.com");
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var anEvent = Event.newEvent("Disney", "2024-01-01", 100, aPartner);

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

        final var eventRepository = new InMemoryEventRepository();
        eventRepository.create(anEvent);

        final var ticketRepository = new InMemoryTicketRepository();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), aCustomer.getCustomerId().value());

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);

        final var output = useCase.execute(subscribeInput);
        // then

        Assertions.assertEquals(anEvent.getEventId().value(), output.eventId());
        Assertions.assertNotNull(output.ticketId());
        Assertions.assertNotNull(output.reservationDate());
        Assertions.assertEquals(TicketStatus.PENDING.name(), output.ticketStatus());

        final var actualEvent = eventRepository.eventOfId(anEvent.getEventId());
        Assertions.assertEquals(expectedTicketsSize, actualEvent.get().allTickets().size());
    }

    @Test
    @DisplayName("Não deve comprar um ticket de um evento que não existe")
    public void testReserveTicketWithoutEvent() throws Exception {
        // given
        final var expectedErrorMessage = "Event not found";

        final var aCustomer = Customer.newCustomer("John Doe", "811.104.200-05", "john.doe@gmail.com");
        final var anEventId = EventId.unique().value();

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEventId, aCustomer.getCustomerId().value());

        // when
        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);

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

        final var aCustomerId = CustomerId.unique().value();
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var anEvent = Event.newEvent("Disney", "2024-01-01", 100, aPartner);

        final var customerRepository = new InMemoryCustomerRepository();
        final var eventRepository = new InMemoryEventRepository();
        final var ticketRepository = new InMemoryTicketRepository();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), aCustomerId);

        // when

        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    @DisplayName("Um mesmo cliente não pode comprar mais de um ticket por evento")
    public void testReserveTickerMoreThanOnce() throws Exception {
        // given
        final var expectedTicketSize = 1;

        final var expectedErrorMessage = "Email already subscribed";

        final var aCustomer = Customer.newCustomer("John Doe", "811.104.200-05", "john.doe@gmail.com");
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var anEvent = Event.newEvent("Disney", "2024-01-01", 100, aPartner);
        final var aTicket = anEvent.reserveTicket(aCustomer.getCustomerId());

        Assertions.assertEquals(expectedTicketSize, anEvent.allTickets().size());

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

        final var eventRepository = new InMemoryEventRepository();
        eventRepository.create(anEvent);

        final var ticketRepository = new InMemoryTicketRepository();
        ticketRepository.create(aTicket);

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), aCustomer.getCustomerId().value());

        // when

        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        final var actualEvent = eventRepository.eventOfId(anEvent.getEventId());
        Assertions.assertEquals(expectedTicketSize, actualEvent.get().allTickets().size());
    }

    @Test
    @DisplayName("Não deve comprar um ticket para um evento esgotado")
    public void testReserveTicketForSoldOutEventShouldFail() throws Exception {
        // given
        final var expectedErrorMessage = "Event sold out";

        final var aCustomer = Customer.newCustomer("John Doe", "811.104.200-05", "john.doe@gmail.com");
        final var aPartner = Partner.newPartner("John Doe", "55.918.450/0001-50", "john.doe@gmail.com");
        final var anEvent = Event.newEvent("Disney", "2024-01-01", 0, aPartner);

        final var customerRepository = new InMemoryCustomerRepository();
        customerRepository.create(aCustomer);

        final var eventRepository = new InMemoryEventRepository();
        eventRepository.create(anEvent);

        final var ticketRepository = new InMemoryTicketRepository();

        final var subscribeInput = new SubscribeCustomerToEventUseCase.Input(anEvent.getEventId().value(), aCustomer.getCustomerId().value());

        // when

        final var useCase = new SubscribeCustomerToEventUseCase(customerRepository, eventRepository, ticketRepository);

        final var actualException = Assertions.assertThrows(ValidationException.class,
                () -> useCase.execute(subscribeInput));

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
