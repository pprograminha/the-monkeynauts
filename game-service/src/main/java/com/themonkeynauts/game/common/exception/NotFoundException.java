package com.themonkeynauts.game.common.exception;

public class NotFoundException extends TheMonkeynautsException {

    private final String entityName;

    public NotFoundException(String entityName) {
        super("validation.entity.not-found");
        this.entityName = entityName;
    }

    public String getEntityName() {
        return this.entityName;
    }
}
