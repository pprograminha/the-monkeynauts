package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.proto.common.messages.Equipment;
import com.themonkeynauts.proto.common.messages.EquipmentType;

@MapperAdapter
public class InEquipmentMapper {

    public Equipment toProto(com.themonkeynauts.game.domain.Equipment domain) {
        var type = domain.isWeapon() ? EquipmentType.WEAPON
                : domain.isShield() ? EquipmentType.SHIELD
                : EquipmentType.ACCESSORY;
        return Equipment.newBuilder()
                .setId(domain.getId().toString())
                .setName(domain.getName())
                .setType(type)
                .build();
    }

}
