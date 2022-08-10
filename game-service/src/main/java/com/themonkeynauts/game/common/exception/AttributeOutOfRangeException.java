package com.themonkeynauts.game.common.exception;

public class AttributeOutOfRangeException extends RuntimeException {

    private Class clazz;

    public AttributeOutOfRangeException(Class clazz) {
        super("validation.attribute.out-of-range");
        this.clazz = clazz;
    }
}
