package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.proto.common.messages.Ship;
import com.themonkeynauts.proto.common.messages.ShipAttributes;
import com.themonkeynauts.proto.common.messages.ShipBonus;
import com.themonkeynauts.proto.common.messages.ShipCrew;
import lombok.RequiredArgsConstructor;

@MapperAdapter
@RequiredArgsConstructor
public class InShipMapper {

    private final InShipClassMapper classTypeMapper;
    private final InShipRankMapper rankTypeMapper;
    private final InMonkeynautMapper monkeynautMapper;
    private final InUserMapper userMapper;

    public Ship toProto(com.themonkeynauts.game.domain.Ship domain) {
        var clazz = classTypeMapper.toProto(domain.getClazz());
        var rank = rankTypeMapper.toProto(domain.getRank());
        var attributes = ShipAttributes.newBuilder().setCurrentDurability(domain.currentDurability()).setMaxDurability(domain.maxDurability()).build();
        var monkeynauts = domain.getCrew().monkeynauts()
                .stream().map(monkeynaut -> monkeynautMapper.toProto(monkeynaut))
                .toList();
        var protoBuilder = Ship.newBuilder()
                .setId(domain.getId().toString())
                .setName(domain.getName())
                .setClass_(clazz)
                .setRank(rank)
                .setAttributes(attributes)
                .setBonus(ShipBonus.newBuilder().setDescription(domain.bonus().getDescription()).setValue(domain.bonus().getValue()).build())
                .setCrew(ShipCrew.newBuilder().setSeats(domain.getCrew().seats()).addAllMonkeynauts(monkeynauts).build());
        if (domain.getOwner() != null) {
            protoBuilder.setOwner(userMapper.toProto(domain.getOwner()));
        }
        if (domain.getOperator() != null) {
            protoBuilder.setOperator(userMapper.toProto(domain.getOperator()));
        }
        return protoBuilder.build();
    }

    public InShipClassMapper getClassTypeMapper() {
        return this.classTypeMapper;
    }

    public InShipRankMapper getRankTypeMapper() {
        return this.rankTypeMapper;
    }
}
