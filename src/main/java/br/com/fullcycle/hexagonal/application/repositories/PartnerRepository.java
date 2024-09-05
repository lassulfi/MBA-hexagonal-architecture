package br.com.fullcycle.hexagonal.application.repositories;

import java.util.Optional;

import br.com.fullcycle.hexagonal.application.domain.partner.Partner;
import br.com.fullcycle.hexagonal.application.domain.partner.PartnerId;
import br.com.fullcycle.hexagonal.application.domain.person.Cnpj;
import br.com.fullcycle.hexagonal.application.domain.person.Email;

public interface PartnerRepository {
    Optional<Partner> partnerOfId(PartnerId anId);

    Optional<Partner> partnerOfCnpj(Cnpj aCnpj);

    Optional<Partner> partnerOfEmail(Email anEmail);

    Partner create(Partner aPartner);

    Partner update(Partner aPartner);

    void deleteAll();
}
