package com.themonkeynauts.game.common.exception;

import java.util.UUID;

public class EquipmentNotFoundException extends RuntimeException {

    private final UUID id;

    public EquipmentNotFoundException(UUID id) {
        super("validation.equipment.not-found");
        this.id = id;
    }

    public UUID getId() {
        return this.id;
    }
}
