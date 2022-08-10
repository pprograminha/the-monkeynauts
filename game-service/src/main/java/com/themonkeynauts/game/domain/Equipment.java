package com.themonkeynauts.game.domain;

import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

@ToString
public class Equipment {

    private UUID id;

    private final String name;
    private final EquipmentType type;

    public Equipment(UUID id, String name, EquipmentType type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public EquipmentType getType() {
        return this.type;
    }

    public boolean isWeapon() {
        return EquipmentType.WEAPON.equals(this.type);
    }

    public boolean isShield() {
        return EquipmentType.SHIELD.equals(this.type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Equipment that = (Equipment) o;

        return new EqualsBuilder().append(id, that.id).append(name, that.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).toHashCode();
    }
}
