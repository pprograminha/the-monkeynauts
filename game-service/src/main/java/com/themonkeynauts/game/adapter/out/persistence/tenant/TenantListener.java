package com.themonkeynauts.game.adapter.out.persistence.tenant;

import com.themonkeynauts.game.adapter.in.web.rpc.tenant.TenantContext;
import com.themonkeynauts.game.adapter.out.persistence.entity.TenantEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

@Component
public class TenantListener {

    private static OutAccountMapper accountMapper;

    @Autowired
    public void setAccountMapper(OutAccountMapper accountMapper) {
        TenantListener.accountMapper = accountMapper;
    }

    @PreUpdate
    @PreRemove
    @PrePersist
    public void setTenant(Object entity) {
        if (entity instanceof TenantEntity) {
            var tenantEntity = (TenantEntity) entity;
            if (tenantEntity.getTenant() == null) {
                var tenant = TenantContext.currentTenant();
                var accountEntity = accountMapper.toEntity(tenant);
                tenantEntity.setTenant(accountEntity);
            }
        }
    }
}
