package br.com.fullcycle.hexagonal.application.usecases.customer;

import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;
import br.com.fullcycle.hexagonal.application.usecases.UseCase;

import java.util.Optional;
import java.util.Objects;

public class GetCustomerByIdUseCase
        extends UseCase<GetCustomerByIdUseCase.Input, Optional<GetCustomerByIdUseCase.Output>> {

    private final CustomerRepository customerRepository;

    public GetCustomerByIdUseCase(final CustomerRepository customerRepository) {
        this.customerRepository = Objects.requireNonNull(customerRepository);
    }

    @Override
    public Optional<Output> execute(Input input) {
        return customerRepository.customerOfId(CustomerId.with(input.id))
                .map(c -> new Output(c.getCustomerId().value(), c.getCpf().value(), c.getEmail().value(), c.getName().value()));
    }

    public record Input(String id) {
    }

    public record Output(String id, String cpf, String email, String name) {
    }
}
