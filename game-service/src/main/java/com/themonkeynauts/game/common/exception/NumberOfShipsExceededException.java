package com.themonkeynauts.game.common.exception;

public class NumberOfShipsExceededException extends TheMonkeynautsException {

    public NumberOfShipsExceededException() {
        super("validation.ship.number-exceeded");
    }
}
