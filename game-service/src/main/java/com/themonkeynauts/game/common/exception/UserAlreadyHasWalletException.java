package com.themonkeynauts.game.common.exception;

public class UserAlreadyHasWalletException extends TheMonkeynautsException {

    public UserAlreadyHasWalletException() {
        super("validation.user.already-has-wallet");
    }
}
