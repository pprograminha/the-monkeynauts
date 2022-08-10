package com.themonkeynauts.game.common.exception;

import java.util.UUID;

public class MonkeynautAlreadyInCrewException extends TheMonkeynautsException {

    private UUID shipId;

    public MonkeynautAlreadyInCrewException(UUID shipId) {
        super("validation.ship.monkeynaut-already-in");
        this.shipId = shipId;
    }

    public UUID getShipId() {
        return this.shipId;
    }
}
