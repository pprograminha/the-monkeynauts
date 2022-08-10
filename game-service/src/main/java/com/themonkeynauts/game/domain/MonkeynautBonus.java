package com.themonkeynauts.game.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class MonkeynautBonus {

    private final String description;
    private final float value;

    public MonkeynautBonus(MonkeynautClass clazz, float value) {
        this.description = clazz.getType().bonusDescription();
        this.value = value;
    }

    public String getDescription() {
        return this.description;
    }

    public float getValue() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonkeynautBonus monkeynautBonus = (MonkeynautBonus) o;

        return new EqualsBuilder().append(value, monkeynautBonus.value).append(description, monkeynautBonus.description).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(description).append(value).toHashCode();
    }
}
