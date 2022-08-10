package com.themonkeynauts.game.common.exception;

public class CredentialsAlreadyTakenException extends RuntimeException {

    private final String data;

    public CredentialsAlreadyTakenException(String data) {
        super("validation.duplicate.credentials");
        this.data = data.toLowerCase();
    }

    public String getData() {
        return this.data;
    }
}
