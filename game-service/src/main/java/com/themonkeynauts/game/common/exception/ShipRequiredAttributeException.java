package com.themonkeynauts.game.common.exception;

public class ShipRequiredAttributeException extends RuntimeException {

    private final String attribute;

    public ShipRequiredAttributeException(String attribute) {
        super("validation.required.ship");
        this.attribute = attribute;
    }

    public String getAttribute() {
        return this.attribute;
    }
}
