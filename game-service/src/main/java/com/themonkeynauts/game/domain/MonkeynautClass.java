package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class MonkeynautClass {

    private final MonkeynautClassType type;

    public MonkeynautClass(MonkeynautClassType type) {
        if (type == null) {
            throw new MonkeynautRequiredAttributeException("class");
        }
        this.type = type;
    }

    public boolean isEngineer() {
        return MonkeynautClassType.ENGINEER.equals(type);
    }

    public boolean isScientist() {
        return MonkeynautClassType.SCIENTIST.equals(type);
    }

    public boolean isSoldier() {
        return MonkeynautClassType.SOLDIER.equals(type);
    }

    public MonkeynautClassType getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonkeynautClass that = (MonkeynautClass) o;

        return new EqualsBuilder().append(type, that.type).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(type).toHashCode();
    }
}
