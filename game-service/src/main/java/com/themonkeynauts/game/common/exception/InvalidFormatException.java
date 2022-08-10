package com.themonkeynauts.game.common.exception;

public class InvalidFormatException extends RuntimeException {

    private final String type;
    private final String data;

    public InvalidFormatException(String type, String data) {
        super("validation.attribute.invalid");
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }
}
