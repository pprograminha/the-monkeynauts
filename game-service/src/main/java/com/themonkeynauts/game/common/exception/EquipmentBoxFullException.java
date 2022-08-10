package com.themonkeynauts.game.common.exception;

public class EquipmentBoxFullException extends RuntimeException {

    public EquipmentBoxFullException() {
        super("validation.equipment-box.full");
    }
}
