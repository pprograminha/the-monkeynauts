package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Wallet;

public interface SaveWalletPort {

    Wallet save(Wallet wallet);
}
