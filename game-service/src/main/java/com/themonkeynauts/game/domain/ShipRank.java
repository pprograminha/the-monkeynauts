package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.ShipRequiredAttributeException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class ShipRank {

    private final ShipRankType type;

    public ShipRank(ShipRankType type) {
        if (type == null) {
            throw new ShipRequiredAttributeException("rank");
        }
        this.type = type;
    }

    public ShipRankType getType() {
        return this.type;
    }

    public int seats() {
        return this.type.seats();
    }

    public boolean isRankB() {
        return ShipRankType.B.equals(type);
    }

    public boolean isRankA() {
        return ShipRankType.A.equals(type);
    }

    public boolean isRankS() {
        return ShipRankType.S.equals(type);
    }

    public float bountyHuntingMissionBonus() {
        return this.type.bountyHuntingMissionBonus();
    }

    public float miningMissionBonus() {
        return this.type.miningMissionBonus();
    }

    public float explorationMissionBonus() {
        return this.type.explorationMissionBonus();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShipRank shipRank = (ShipRank) o;

        return new EqualsBuilder().append(type, shipRank.type).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).toHashCode();
    }
}
