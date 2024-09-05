package br.com.fullcycle.hexagonal.application.usecases.event;

import java.util.Objects;

import br.com.fullcycle.hexagonal.application.domain.event.Event;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.exceptions.ValidationException;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;

public class CreateEventUseCase extends UseCase<CreateEventUseCase.Input, CreateEventUseCase.Output> {

    private final PartnerRepository partnerRepository;

    private final EventRepository eventRepository;

    public CreateEventUseCase(final PartnerRepository partnerRepository, final EventRepository eventRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
        this.eventRepository = Objects.requireNonNull(eventRepository);
    }

    @Override
    public Output execute(Input input) {
        final var partner = partnerRepository.partnerOfId(PartnerId.with(input.partnerId))
                .orElseThrow(() -> new ValidationException("Partner not found"));

        final var event = eventRepository.create(Event.newEvent(input.name, input.date, input.totalSpots, partner));

        return new Output(
            event.getEventId().value(), 
            event.getDate().toString(), 
            event.getName().value(),
            event.getTotalSpots(), 
            event.getPartnerId().value());
    }

    public record Input(String date, String name, String partnerId, Integer totalSpots) {
    }

    public record Output(String id, String date, String name, Integer totalSpots, String partnerId) {
    }
}
