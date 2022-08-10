package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Account;

public interface SaveAccountPort {

    Account save(Account account);
}
