package com.themonkeynauts.game.adapter.out.persistence.entity;

import com.themonkeynauts.game.adapter.out.persistence.tenant.TenantListener;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Embeddable;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(TenantListener.class)
@SuperBuilder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public abstract class AbstractTenantEntity implements TenantEntity, Serializable {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private AccountEntity account;

    public AccountEntity getTenant() {
        return this.account;
    }

    public void setTenant(AccountEntity tenant) {
        this.account = tenant;
    }

}
