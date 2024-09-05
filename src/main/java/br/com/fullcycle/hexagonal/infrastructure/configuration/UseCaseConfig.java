package br.com.fullcycle.hexagonal.infrastructure.configuration;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.repositories.EventRepository;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.repositories.TicketRepository;
import br.com.fullcycle.hexagonal.application.usecases.customer.CreateCustomerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.CreateEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.CreatePartnerUseCase;
import br.com.fullcycle.hexagonal.application.usecases.partner.GetPartnerByIdUseCase;

@Configuration
public class UseCaseConfig {

    private final CustomerRepository customerRepository;
    private final PartnerRepository partnerRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;

    public UseCaseConfig(
        final CustomerRepository customerRepository, 
        final PartnerRepository partnerRepository,
        final EventRepository eventRepository,
        final TicketRepository ticketRepository
    ) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
        this.eventRepository = Objects.requireNonNull(eventRepository);
        this.ticketRepository = Objects.requireNonNull(ticketRepository);
    }

    @Bean
    CreateCustomerUseCase createCustomerUseCase() {
        return new CreateCustomerUseCase(this.customerRepository);
    }

    @Bean
    CreateEventUseCase createEventUseCase() {
        return new CreateEventUseCase(
            this.partnerRepository, 
            this.eventRepository);
    }

    @Bean
    CreatePartnerUseCase createPartnerUseCase() {
        return new CreatePartnerUseCase(this.partnerRepository);
    }

    @Bean
    GetCustomerByIdUseCase getCustomerByIdUseCase() {
        return new GetCustomerByIdUseCase(this.customerRepository);
    }

    @Bean
    GetPartnerByIdUseCase getPartnerByIdUseCase() {
        return new GetPartnerByIdUseCase(this.partnerRepository);
    }

    @Bean
    SubscribeCustomerToEventUseCase subscribeCustomerToEventUseCase() {
        return new SubscribeCustomerToEventUseCase(
            this.customerRepository, 
            this.eventRepository, 
            this.ticketRepository);
    }
}
