package com.themonkeynauts.game.common.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException() {
        super("validation.authentication.invalid-credentials");
    }
}
