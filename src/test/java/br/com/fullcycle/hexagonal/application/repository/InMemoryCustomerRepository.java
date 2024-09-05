package br.com.fullcycle.hexagonal.application.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.customer.Customer;
import br.com.fullcycle.hexagonal.application.domain.customer.CustomerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cpf;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.CustomerRepository;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<String, Customer> customers;
    private final Map<String, Customer> customersByCPF;
    private final Map<String, Customer> customersByEmail;

    public InMemoryCustomerRepository() {
        this.customers = new HashMap<>();
        this.customersByCPF = new HashMap<>();
        this.customersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Customer> customerOfId(CustomerId anId) {
        return Optional.ofNullable(this.customers.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Optional<Customer> customerOfCpf(final Cpf aCpf) {
        return Optional.ofNullable(this.customersByCPF.get(Objects.requireNonNull(aCpf.value())));
    }

    @Override
    public Optional<Customer> customerOfEmail(final Email anEmail) {
        return Optional.ofNullable(this.customersByCPF.get(Objects.requireNonNull(anEmail.value())));
    }

    @Override
    public Customer create(Customer aCustomer) {
        this.customers.put(aCustomer.getCustomerId().value().toString(), aCustomer);
        this.customersByCPF.put(aCustomer.getCpf().value(), aCustomer);
        this.customersByEmail.put(aCustomer.getEmail().value(), aCustomer);

        return aCustomer;
    }

    @Override
    public Customer update(Customer aCustomer) {
        this.customers.put(aCustomer.getCustomerId().value().toString(), aCustomer);
        this.customersByCPF.put(aCustomer.getCpf().value(), aCustomer);
        this.customersByEmail.put(aCustomer.getEmail().value(), aCustomer);

        return aCustomer;
    }

    @Override
    public void deleteAll() {
        this.customers.clear();
        this.customersByCPF.clear();
        this.customersByEmail.clear();
    }
}