package br.com.fullcycle.hexagonal.application.usecases.partner;

import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;

import java.util.Optional;
import java.util.Objects;

public class GetPartnerByIdUseCase
        extends UseCase<GetPartnerByIdUseCase.Input, Optional<GetPartnerByIdUseCase.Output>> {

    final PartnerRepository partnerRepository;

    public GetPartnerByIdUseCase(final PartnerRepository partnerRepository) {
        this.partnerRepository = Objects.requireNonNull(partnerRepository);
    }

    @Override
    public Optional<Output> execute(final Input input) {
        return partnerRepository.partnerOfId(PartnerId.with(input.id))
                .map(p -> 
                    new Output(
                        p.getPartnerId().value(), 
                        p.getName().value(), 
                        p.getCnpj().value(), 
                        p.getEmail().value()));
    }

    public record Input(String id) {
    }

    public record Output(String id, String name, String cnpj, String email) {
    }
}
