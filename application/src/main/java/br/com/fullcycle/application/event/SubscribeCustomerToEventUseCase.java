package br.com.fullcycle.application.event;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.domain.customer.CustomerId;
import br.com.fullcycle.domain.event.EventId;
import br.com.fullcycle.domain.exceptions.ValidationException;
import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.application.UseCase;

public class SubscribeCustomerToEventUseCase
        extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

    private final CustomerRepository customerRepository;

    private final EventRepository eventRepository;

    public SubscribeCustomerToEventUseCase(
        final CustomerRepository customerRepository, 
        final EventRepository eventRepository
    ) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    @Override
    public Output execute(final Input input) {
        var aCustomer = customerRepository.customerOfId(CustomerId.with(input.customerId))
            .orElseThrow(() -> new ValidationException("Customer not found"));

        var anEvent = eventRepository.eventOfId(EventId.with(input.eventId))
            .orElseThrow(() -> new ValidationException("Event not found"));

        final var aTicket = anEvent.reserveTicket(aCustomer.getCustomerId());

        this.eventRepository.update(anEvent);

        return new Output(
            anEvent.getEventId().value(), 
            aTicket.getEventTicketId().value(), 
            Instant.now());
    }

    public record Input(String eventId, String customerId) {
    }

    public record Output(String eventId, String eventTicketId, Instant reservationDate) {
    }
}
