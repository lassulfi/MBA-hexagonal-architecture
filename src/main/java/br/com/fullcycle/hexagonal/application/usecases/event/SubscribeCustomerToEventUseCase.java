package br.com.fullcycle.hexagonal.application.usecases.event;

import java.time.Instant;
import java.util.Objects;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.event.EventId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;

public class SubscribeCustomerToEventUseCase
        extends UseCase<SubscribeCustomerToEventUseCase.Input, SubscribeCustomerToEventUseCase.Output> {

    private final CustomerRepository customerRepository;

    private final EventRepository eventRepository;

    private final TicketRepository ticketRepository;

    public SubscribeCustomerToEventUseCase(
        final CustomerRepository customerRepository, 
        final EventRepository eventRepository,
        final TicketRepository ticketRepository
    ) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.ticketRepository = Objects.requireNonNull(ticketRepository);
    }

    @Override
    public Output execute(final Input input) {
        var aCustomer = customerRepository.customerOfId(CustomerId.with(input.customerId))
            .orElseThrow(() -> new ValidationException("Customer not found"));

        var anEvent = eventRepository.eventOfId(EventId.with(input.eventId))
            .orElseThrow(() -> new ValidationException("Event not found"));

        final var aTicket = anEvent.reserveTicket(aCustomer.getCustomerId());

        this.ticketRepository.create(aTicket);
        this.eventRepository.update(anEvent);

        return new Output(
            anEvent.getEventId().value(), 
            aTicket.getTicketId().value(), 
            aTicket.getTicketStatus().name(), 
            aTicket.getReservedAt());
    }

    public record Input(String eventId, String customerId) {
    }

    public record Output(String eventId, String ticketId, String ticketStatus, Instant reservationDate) {
    }
}
