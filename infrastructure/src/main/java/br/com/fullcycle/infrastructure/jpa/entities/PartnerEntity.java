package br.com.fullcycle.infrastructure.jpa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

import br.com.fullcycle.domain.partner.Partner;
import br.com.fullcycle.domain.partner.PartnerId;

@Entity(name = "Partner")
@Table(name = "partners")
public class PartnerEntity {

    @Id
    private UUID id;

    private String name;

    private String cnpj;

    private String email;

    public PartnerEntity() {
    }

    public PartnerEntity(UUID id, String name, String cnpj, String email) {
        this.id = id;
        this.name = name;
        this.cnpj = cnpj;
        this.email = email;
    }

    public static PartnerEntity of(final Partner aPartner) {
        return new PartnerEntity(
            UUID.fromString(aPartner.getPartnerId().value()), 
            aPartner.getName().value(), 
            aPartner.getCnpj().value(), 
            aPartner.getEmail().value());
    }

    public Partner toPartner() {
        return new Partner(PartnerId.with(this.id.toString()), this.name, this.cnpj, this.email);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PartnerEntity other = (PartnerEntity) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }    
}
