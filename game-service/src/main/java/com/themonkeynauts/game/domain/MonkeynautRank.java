package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class MonkeynautRank {

    private final MonkeynautRankType type;

    public MonkeynautRank(MonkeynautRankType type) {
        if (type == null) {
            throw new MonkeynautRequiredAttributeException("rank");
        }
        this.type = type;
    }

    public MonkeynautRankType getType() {
        return this.type;
    }

    public boolean isPrivate() {
        return MonkeynautRankType.PRIVATE.equals(type);
    }

    public boolean isSergeant() {
        return MonkeynautRankType.SERGEANT.equals(type);
    }

    public boolean isCaptain() {
        return MonkeynautRankType.CAPTAIN.equals(type);
    }

    public boolean isMajor() {
        return MonkeynautRankType.MAJOR.equals(type);
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

        MonkeynautRank that = (MonkeynautRank) o;

        return new EqualsBuilder().append(type, that.type).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).toHashCode();
    }
}
