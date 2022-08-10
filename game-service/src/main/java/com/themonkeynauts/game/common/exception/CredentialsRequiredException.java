package com.themonkeynauts.game.common.exception;

public class CredentialsRequiredException extends RuntimeException {

    public CredentialsRequiredException() {
        super("validation.required.credentials");
    }
}
