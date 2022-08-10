package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.ShipClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipRankEnum;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import com.themonkeynauts.game.domain.ShipRankType;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@MapperAdapter
@RequiredArgsConstructor
public class OutShipMapper {

    private final OutUserMapper userMapper;
    private final OutMonkeynautMapper monkeynautMapper;

    public ShipEntity toEntity(Ship domain) {
        var clazz = domain.getClazz().isFighter() ? ShipClassEnum.FIGHTER
                : domain.getClazz().isMiner() ? ShipClassEnum.MINER
                : ShipClassEnum.EXPLORER;
        var rank = domain.getRank().isRankS() ? ShipRankEnum.S
                : domain.getRank().isRankA() ? ShipRankEnum.A
                : ShipRankEnum.B;
        var crew = domain.getCrew().monkeynauts().stream()
                .map(monkeynaut -> monkeynautMapper.toEntity(monkeynaut))
                .collect(Collectors.toSet());
        return ShipEntity.builder()
                .id(domain.getId())
                .name(domain.getName())
                .durability(domain.getAttributes().getCurrentDurability().getValue())
                .clazz(clazz)
                .rank(rank)
                .monkeynauts(crew)
                .user(domain.getOwner() != null ? userMapper.toEntity(domain.getOwner()) : null)
                .operator(domain.getOperator() != null ? userMapper.toEntity(domain.getOperator()) : null)
                .build();
    }

    public Ship toDomain(ShipEntity entity) {
        var classType = ShipClassEnum.FIGHTER.equals(entity.getClazz()) ? ShipClassType.FIGHTER
                : ShipClassEnum.MINER.equals(entity.getClazz()) ? ShipClassType.MINER
                : ShipClassType.EXPLORER;
        var rankType = ShipRankEnum.S.equals(entity.getRank()) ? ShipRankType.S
                : ShipRankEnum.A.equals(entity.getRank()) ? ShipRankType.A
                : ShipRankType.B;
        var durability = new ShipDurabilityAttribute(entity.getDurability());
        var attributes = new ShipAttributes(durability);
        var ship = new Ship(entity.getId(), entity.getName(), new ShipClass(classType), new ShipRank(rankType), attributes);
        ship.setOwner(userMapper.toDomain(entity.getUser()));
        ship.setOperator(userMapper.toDomain(entity.getOperator()));

        entity.getMonkeynauts().stream()
                .map(monkeynautEntity -> monkeynautMapper.toDomain(monkeynautEntity))
                .forEach(monkeynaut -> ship.getCrew().addMonkeynaut(monkeynaut));

        return ship;
    }
}
