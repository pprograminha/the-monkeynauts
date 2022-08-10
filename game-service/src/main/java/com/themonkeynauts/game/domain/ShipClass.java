package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.ShipRequiredAttributeException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class ShipClass {

    private final ShipClassType type;

    public ShipClass(ShipClassType type) {
        if (type == null) {
            throw new ShipRequiredAttributeException("class");
        }
        this.type = type;
    }

    public ShipClassType getType() {
        return this.type;
    }

    public boolean isFighter() {
        return ShipClassType.FIGHTER.equals(type);
    }

    public boolean isMiner() {
        return ShipClassType.MINER.equals(type);
    }

    public boolean isExplorer() {
        return ShipClassType.EXPLORER.equals(type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShipClass shipClass = (ShipClass) o;

        return new EqualsBuilder().append(type, shipClass.type).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).toHashCode();
    }
}
