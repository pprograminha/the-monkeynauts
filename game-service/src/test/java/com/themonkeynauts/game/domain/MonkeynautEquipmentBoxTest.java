package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.EquipmentBoxFullException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautEquipmentBoxTest {

    private MonkeynautEquipmentBox box;

    @BeforeEach
    public void setUp() {
        box = new MonkeynautEquipmentBox();
    }

    @Test
    public void shouldAddEquipment() {
        var swordId = UUID.randomUUID();
        var sword = new Equipment(swordId, "Sword", EquipmentType.WEAPON);

        var gunId = UUID.randomUUID();
        var gun = new Equipment(gunId, "Gun", EquipmentType.WEAPON);

        box.add(sword);
        box.add(gun);

        assertThat(box.numberOfEquipments()).isEqualTo(2);
    }

    @Test
    public void shouldRemoveEquipment() {
        var swordId = UUID.randomUUID();
        var sword = new Equipment(swordId, "Sword", EquipmentType.WEAPON);

        var gunId = UUID.randomUUID();
        var gun = new Equipment(gunId, "Gun", EquipmentType.WEAPON);

        box.add(sword);
        box.add(gun);

        box.remove(sword);

        assertThat(box.numberOfEquipments()).isEqualTo(1);
        assertThat(box.has(sword)).isFalse();
        assertThat(box.has(gun)).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfBoxIsFull() {
        var swordId = UUID.randomUUID();
        var sword = new Equipment(swordId, "Sword", EquipmentType.WEAPON);

        var gunId = UUID.randomUUID();
        var gun = new Equipment(gunId, "Gun", EquipmentType.WEAPON);

        var shieldId = UUID.randomUUID();
        var shield = new Equipment(shieldId, "Shield", EquipmentType.SHIELD);

        box.add(sword);
        box.add(gun);
        box.add(shield);

        assertThatThrownBy(() -> {
            var extraEquipment = new Equipment(UUID.randomUUID(), "Extra", EquipmentType.ACCESSORY);
            box.add(extraEquipment);
        })
        .isInstanceOf(EquipmentBoxFullException.class)
        .hasMessage("validation.equipment-box.full");
    }

}