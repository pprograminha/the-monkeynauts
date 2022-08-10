package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.proto.common.messages.MonkeynautClass;

@MapperAdapter
public class InMonkeynautClassMapper {

    public com.themonkeynauts.game.domain.MonkeynautClass toDomain(MonkeynautClass clazz) {
        return MonkeynautClass.ENGINEER.equals(clazz) ? new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.ENGINEER)
                : MonkeynautClass.SCIENTIST.equals(clazz) ? new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SCIENTIST)
                : MonkeynautClass.SOLDIER.equals(clazz) ? new com.themonkeynauts.game.domain.MonkeynautClass(MonkeynautClassType.SOLDIER)
                : new com.themonkeynauts.game.domain.MonkeynautClass(null);
    }

    public MonkeynautClass toProto(com.themonkeynauts.game.domain.MonkeynautClass domain) {
        return domain.isEngineer() ? MonkeynautClass.ENGINEER
                : domain.isScientist() ? MonkeynautClass.SCIENTIST
                : domain.isSoldier() ? MonkeynautClass.SOLDIER
                : MonkeynautClass.MONKEYNAUT_CLASS_UNSPECIFIED;
    }
}
