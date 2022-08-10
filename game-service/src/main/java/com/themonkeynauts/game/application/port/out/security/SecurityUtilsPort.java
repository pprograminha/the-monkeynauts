package com.themonkeynauts.game.application.port.out.security;

import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.User;

public interface SecurityUtilsPort {

    User getAuthenticatedUser();

    void setTenant(Account account);

    Account getTenant();
}
