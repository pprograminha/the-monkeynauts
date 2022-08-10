package com.themonkeynauts.game.common.exception;

public class WalletAlreadyAssociatedException extends TheMonkeynautsException {

    public WalletAlreadyAssociatedException() {
        super("validation.wallet.already-used");
    }
}
