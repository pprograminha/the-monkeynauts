package com.themonkeynauts.game.common.exception;

public class NotEnoughDurabilityException extends RuntimeException {

    public NotEnoughDurabilityException() {
        super("validation.ship.not-enough-durability");
    }
}
