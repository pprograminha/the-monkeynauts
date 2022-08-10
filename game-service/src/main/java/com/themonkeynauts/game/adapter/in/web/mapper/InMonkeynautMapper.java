package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.proto.common.messages.MonkeynautAttributes;
import com.themonkeynauts.proto.common.messages.MonkeynautBonus;
import lombok.RequiredArgsConstructor;

@MapperAdapter
@RequiredArgsConstructor
public class InMonkeynautMapper {

    private final InMonkeynautClassMapper classTypeMapper;
    private final InMonkeynautRankMapper rankTypeMapper;
    private final InUserMapper userMapper;

    public InMonkeynautClassMapper getClassTypeMapper() {
        return this.classTypeMapper;
    }

    public InMonkeynautRankMapper getRankTypeMapper() {
        return this.rankTypeMapper;
    }

    public com.themonkeynauts.proto.common.messages.Monkeynaut toProto(Monkeynaut domain) {
        var clazz = classTypeMapper.toProto(domain.getClazz());
        var rank = rankTypeMapper.toProto(domain.getRank());
        var attributes = MonkeynautAttributes.newBuilder()
                .setSkill(domain.skill())
                .setSpeed(domain.speed())
                .setResistance(domain.resistance())
                .setLife(domain.life())
                .setCurrentEnergy(domain.currentEnergy())
                .setMaxEnergy(domain.maxEnergy())
                .build();
        var protoBuilder = com.themonkeynauts.proto.common.messages.Monkeynaut.newBuilder()
                .setId(domain.getId().toString())
                .setNumber(domain.getNumber())
                .setFirstName(domain.getFirstName())
                .setLastName(domain.getLastName())
                .setClass_(clazz)
                .setRank(rank)
                .setAttributes(attributes)
                .setBonus(MonkeynautBonus.newBuilder().setDescription(domain.bonus().getDescription()).setValue(domain.bonus().getValue()).build())
                .setBreedCount(domain.getBreedCount());
        if (domain.getShipId() != null) {
            protoBuilder.setShipId(domain.getShipId().toString());
        }
        if (domain.getPlayer() != null) {
            protoBuilder.setOwner(userMapper.toProto(domain.getPlayer()));
        }
        if (domain.getOperator() != null) {
            protoBuilder.setOperator(userMapper.toProto(domain.getOperator()));
        }
        return protoBuilder.build();
    }
}
