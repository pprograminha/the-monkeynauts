package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.Wallet;

public interface CreateWallet {

    Wallet create(CreateWalletCommand command);
}
