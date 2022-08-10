package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InEquipmentMapperTest {

    private InEquipmentMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new InEquipmentMapper();
    }

    @Test
    public void shouldConvertEquipmentToProto() {
        var equipmentId = UUID.randomUUID();
        var equipment = new Equipment(equipmentId, "Weapon #1", EquipmentType.WEAPON);

        var result = mapper.toProto(equipment);

        assertThat(result.getId()).isEqualTo(equipmentId.toString());
        assertThat(result.getName()).isEqualTo("Weapon #1");
        assertThat(result.getType()).isEqualTo(com.themonkeynauts.proto.common.messages.EquipmentType.WEAPON);
    }

}