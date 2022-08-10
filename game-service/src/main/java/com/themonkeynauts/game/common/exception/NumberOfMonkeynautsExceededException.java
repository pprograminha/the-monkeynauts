package com.themonkeynauts.game.common.exception;

public class NumberOfMonkeynautsExceededException extends TheMonkeynautsException {

    public NumberOfMonkeynautsExceededException() {
        super("validation.monkeynaut.number-exceeded");
    }
}
