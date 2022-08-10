package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.proto.common.messages.ShipClass;

@MapperAdapter
public class InShipClassMapper {

    public ShipClass toProto(com.themonkeynauts.game.domain.ShipClass domain) {
        return domain.isFighter() ? ShipClass.FIGHTER
                : domain.isMiner() ? ShipClass.MINER
                : domain.isExplorer() ? ShipClass.EXPLORER
                : ShipClass.SHIP_CLASS_UNSPECIFIED;
    }

    public com.themonkeynauts.game.domain.ShipClass toDomain(ShipClass proto) {
        return ShipClass.FIGHTER.equals(proto) ? new com.themonkeynauts.game.domain.ShipClass(ShipClassType.FIGHTER)
                : ShipClass.MINER.equals(proto) ? new com.themonkeynauts.game.domain.ShipClass(ShipClassType.MINER)
                : ShipClass.EXPLORER.equals(proto) ? new com.themonkeynauts.game.domain.ShipClass(ShipClassType.EXPLORER)
                : new com.themonkeynauts.game.domain.ShipClass(null);
    }
}
