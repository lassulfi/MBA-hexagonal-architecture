package br.com.fullcycle.infrastructure.repositories;

import java.util.Optional;
import java.util.Objects;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;
import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;
import br.com.fullcycle.domain.partner.PartnerRepository;
import br.com.fullcycle.infrastructure.jpa.entities.PartnerEntity;
import br.com.fullcycle.infrastructure.jpa.repositories.PartnerJpaRepository;
import java.util.UUID;


// Interface adapter
@Component
public class PartnerDatabaseRepository implements PartnerRepository {

    private final PartnerJpaRepository partnerJpaRepository;

    public PartnerDatabaseRepository(PartnerJpaRepository partnerJpaRepository) {
        this.partnerJpaRepository = Objects.requireNonNull(partnerJpaRepository);
    }

    @Override
    public Optional<Partner> partnerOfId(final PartnerId anId) {
        Objects.requireNonNull(anId, "id cannot be null");
        return this.partnerJpaRepository.findById(UUID.fromString(anId.value())).map(it -> it.toPartner());
    }

    @Override
    public Optional<Partner> partnerOfCnpj(final Cnpj aCnpj) {
        Objects.requireNonNull(aCnpj, "CNPJ cannot be null");
        return this.partnerJpaRepository.findByCnpj(aCnpj.value()).map(it -> it.toPartner());
    }

    @Override
    public Optional<Partner> partnerOfEmail(final Email anEmail) {
        Objects.requireNonNull(anEmail, "email cannot be null");
        return this.partnerJpaRepository.findByEmail(anEmail.value()).map(it -> it.toPartner());
    }

    @Override
    @Transactional
    public Partner create(final Partner aPartner) {
        return this.partnerJpaRepository.save(PartnerEntity.of(aPartner)).toPartner();
    }

    @Override
    @Transactional
    public Partner update(final Partner aPartner) {
        return this.partnerJpaRepository.save(PartnerEntity.of(aPartner)).toPartner();
    }

    @Override
    public void deleteAll() {
        this.partnerJpaRepository.deleteAll();
    }
    
}
