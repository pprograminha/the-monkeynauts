package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.NotEnoughDurabilityException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class ShipDurabilityAttribute {

    private int value;

    public ShipDurabilityAttribute(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void decrease(int amount) {
        if (this.value < amount) {
            throw new NotEnoughDurabilityException();
        }
        this.value = this.value - amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShipDurabilityAttribute that = (ShipDurabilityAttribute) o;

        return new EqualsBuilder().append(value, that.value).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(value).toHashCode();
    }
}
