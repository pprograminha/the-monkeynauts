package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEnum;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;

@MapperAdapter
public class OutEquipmentMapper {

    public EquipmentEntity toEntity(Equipment domain) {
        var type = domain.isWeapon() ? EquipmentEnum.WEAPON
                : domain.isShield() ? EquipmentEnum.SHIELD
                : EquipmentEnum.ACCESSORY;
        return EquipmentEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .type(type)
                .build();
    }

    public Equipment toDomain(EquipmentEntity entity) {
        var type = EquipmentType.valueOf(entity.getType().name());
        return new Equipment(entity.getId(), entity.getName(), type);
    }
}
