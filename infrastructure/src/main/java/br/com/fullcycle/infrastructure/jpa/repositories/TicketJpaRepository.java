package br.com.fullcycle.infrastructure.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import br.com.fullcycle.infrastructure.jpa.entities.TicketEntity;

import java.util.Optional;
import java.util.UUID;

public interface TicketJpaRepository extends CrudRepository<TicketEntity, UUID> {

    Optional<TicketEntity> findByEventIdAndCustomerId(UUID id, UUID customerId);
}
