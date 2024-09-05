package br.com.fullcycle.hexagonal.application.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;
import br.com.fullcycle.hexagonal.application.repositories.PartnerRepository;

public class InMemoryPartnerRepository implements PartnerRepository {

    private final Map<String, Partner> partners;
    private final Map<String, Partner> partnersByCNPJ;
    private final Map<String, Partner> partnersByEmail;

    public InMemoryPartnerRepository() {
        this.partners = new HashMap<>();
        this.partnersByCNPJ = new HashMap<>();
        this.partnersByEmail = new HashMap<>();
    }

    @Override
    public Optional<Partner> partnerOfId(PartnerId anId) {
        return Optional.ofNullable(this.partners.get(Objects.requireNonNull(anId).value().toString()));
    }

    @Override
    public Optional<Partner> partnerOfCnpj(final Cnpj aCNPJ) {
        return Optional.ofNullable(this.partnersByCNPJ.get(Objects.requireNonNull(aCNPJ.value())));
    }

    @Override
    public Optional<Partner> partnerOfEmail(final Email anEmail) {
        return Optional.ofNullable(this.partnersByCNPJ.get(Objects.requireNonNull(anEmail.value())));
    }

    @Override
    public Partner create(Partner aPartner) {
        this.partners.put(aPartner.getPartnerId().value().toString(), aPartner);
        this.partnersByCNPJ.put(aPartner.getCnpj().value(), aPartner);
        this.partnersByEmail.put(aPartner.getEmail().value(), aPartner);

        return aPartner;
    }

    @Override
    public Partner update(Partner aPartner) {
        this.partners.put(aPartner.getPartnerId().value().toString(), aPartner);
        this.partnersByCNPJ.put(aPartner.getCnpj().value(), aPartner);
        this.partnersByEmail.put(aPartner.getEmail().value(), aPartner);

        return aPartner;
    }

    @Override
    public void deleteAll() {
        this.partners.clear();
        this.partnersByCNPJ.clear();
        this.partnersByEmail.clear();
    }
}