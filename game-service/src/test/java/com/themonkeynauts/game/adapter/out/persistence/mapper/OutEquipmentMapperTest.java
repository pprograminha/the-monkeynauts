package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEnum;
import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OutEquipmentMapperTest {

    private OutEquipmentMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new OutEquipmentMapper();
    }

    @Test
    public void shouldConvertToEntity() {
        var id = UUID.randomUUID();
        var equipment = new Equipment(id, "Weapon #1", EquipmentType.WEAPON);

        var result = mapper.toEntity(equipment);

        assertThat(result).isEqualTo(EquipmentEntity.builder().id(id).name("Weapon #1").type(EquipmentEnum.WEAPON).build());
    }

}