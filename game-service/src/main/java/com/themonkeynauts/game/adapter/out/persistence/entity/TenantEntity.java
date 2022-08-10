package com.themonkeynauts.game.adapter.out.persistence.entity;

import java.io.Serializable;

public interface TenantEntity extends Serializable {

    AccountEntity getTenant();

    void setTenant(AccountEntity tenant);
}