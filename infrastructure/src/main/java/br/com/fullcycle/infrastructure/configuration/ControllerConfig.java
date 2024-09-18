package br.com.fullcycle.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fullcycle.application.partner.CreatePartnerUseCase;
import br.com.fullcycle.application.partner.GetPartnerByIdUseCase;
import br.com.fullcycle.infrastructure.rest.PartnerFnController;

@Configuration
public class ControllerConfig {
 
    @Bean
    PartnerFnController partnerFnController(
        final CreatePartnerUseCase createPartnerUseCase,
        final GetPartnerByIdUseCase getPartnerByIdUseCase
    ) {
        return new PartnerFnController(createPartnerUseCase, getPartnerByIdUseCase);
    }
}
