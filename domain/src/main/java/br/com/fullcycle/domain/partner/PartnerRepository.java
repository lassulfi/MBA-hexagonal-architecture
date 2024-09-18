package br.com.fullcycle.domain.partner;

import java.util.Optional;

import br.com.fullcycle.domain.person.Cnpj;
import br.com.fullcycle.domain.person.Email;

public interface PartnerRepository {
    Optional<Partner> partnerOfId(PartnerId anId);

    Optional<Partner> partnerOfCnpj(Cnpj aCnpj);

    Optional<Partner> partnerOfEmail(Email anEmail);

    Partner create(Partner aPartner);

    Partner update(Partner aPartner);

    void deleteAll();
}
