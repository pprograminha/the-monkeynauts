package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.EquipmentBoxFullException;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

@ToString
public class MonkeynautEquipmentBox {

    private static final int MAX_NUMBER_OF_EQUIPMENTS = 3;

    private final List<Equipment> equipments;

    public MonkeynautEquipmentBox() {
        this.equipments = new ArrayList<>();
    }

    public void add(Equipment equipment) {
        if (this.equipments.size() == MAX_NUMBER_OF_EQUIPMENTS) {
            throw new EquipmentBoxFullException();
        }
        this.equipments.add(equipment);
    }

    public boolean remove(Equipment equipment) {
        return this.equipments.remove(equipment);
    }

    public int numberOfEquipments() {
        return this.equipments.size();
    }

    public boolean has(Equipment equipment) {
        return this.equipments.contains(equipment);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonkeynautEquipmentBox that = (MonkeynautEquipmentBox) o;

        return new EqualsBuilder().append(equipments, that.equipments).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(equipments).toHashCode();
    }
}
