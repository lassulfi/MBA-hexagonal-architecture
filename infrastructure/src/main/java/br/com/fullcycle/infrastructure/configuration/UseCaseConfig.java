package br.com.fullcycle.infrastructure.configuration;

import java.util.Objects;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fullcycle.domain.customer.CustomerRepository;
import br.com.fullcycle.domain.event.EventRepository;
import br.com.fullcycle.domain.event.ticket.TicketRepository;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.application.customer.CreateCustomerUseCase;
import br.com.fullcycle.application.customer.GetCustomerByIdUseCase;
import br.com.fullcycle.application.event.CreateEventUseCase;
import br.com.fullcycle.application.event.SubscribeCustomerToEventUseCase;
import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.application.ticket.CreateTicketForCustomerUseCase;

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
            this.eventRepository);
    }

    @Bean
    CreateTicketForCustomerUseCase createTicketForCustomerUseCase() {
        return new CreateTicketForCustomerUseCase(this.ticketRepository);
    }
}
