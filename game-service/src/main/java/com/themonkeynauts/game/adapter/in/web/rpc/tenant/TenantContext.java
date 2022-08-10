package com.themonkeynauts.game.adapter.in.web.rpc.tenant;

import com.themonkeynauts.game.adapter.in.web.rpc.context.GameContexts;
import com.themonkeynauts.game.domain.Account;
import io.grpc.Context;

public class TenantContext {

    public static Account currentTenant() {
        return GameContexts.TENANT_CONTEXT_KEY.get();
    }

    public static void setCurrentTenant(Account account) {
        Context.current().withValue(GameContexts.TENANT_CONTEXT_KEY, account).attach();
    }
}
