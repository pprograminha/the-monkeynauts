package com.themonkeynauts.game.common.exception;

public class InvalidWalletAddressException extends TheMonkeynautsException {

    public InvalidWalletAddressException() {
        super("validation.wallet.invalid-address");
    }
}
