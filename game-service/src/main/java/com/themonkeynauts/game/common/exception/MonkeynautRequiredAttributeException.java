package com.themonkeynauts.game.common.exception;

public class MonkeynautRequiredAttributeException extends RuntimeException {

    private final String attribute;

    public MonkeynautRequiredAttributeException(String attribute) {
        super("validation.required.monkeynaut");
        this.attribute = attribute;
    }

    public String getAttribute() {
        return this.attribute;
    }
}
