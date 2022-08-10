package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.AttributeOutOfRangeException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class MonkeynautLifeAttribute {

    public static final short LOW_LIMIT = 250;
    public static final short HIGH_LIMIT = 350;

    private final int value;

    public MonkeynautLifeAttribute(int value) {
        if (value < LOW_LIMIT || value > HIGH_LIMIT) {
            throw new AttributeOutOfRangeException(MonkeynautLifeAttribute.class);
        }
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonkeynautLifeAttribute that = (MonkeynautLifeAttribute) o;

        return new EqualsBuilder().append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }
}
