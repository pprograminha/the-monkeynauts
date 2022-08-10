package com.themonkeynauts.game.domain;

import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class ShipAttributes {

    private final ShipDurabilityAttribute currentDurability;

    public ShipAttributes(ShipDurabilityAttribute currentDurability) {
        this.currentDurability = currentDurability;
    }

    public ShipDurabilityAttribute getCurrentDurability() {
        return this.currentDurability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShipAttributes that = (ShipAttributes) o;

        return new EqualsBuilder().append(currentDurability, that.currentDurability).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(currentDurability).toHashCode();
    }
}
