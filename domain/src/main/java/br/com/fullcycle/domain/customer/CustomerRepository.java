package br.com.fullcycle.domain.customer;

import java.util.Optional;

import br.com.fullcycle.domain.person.Cpf;
import br.com.fullcycle.domain.person.Email;

public interface CustomerRepository {
    Optional<Customer> customerOfId(CustomerId anId);

    Optional<Customer> customerOfCpf(Cpf aCpf);

    Optional<Customer> customerOfEmail(Email anEmail);

    Customer create(Customer aCustomer);

    Customer update(Customer aCustomer);

    void deleteAll();
}
