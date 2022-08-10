package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.ShipRankType;
import com.themonkeynauts.proto.common.messages.ShipRank;

@MapperAdapter
public class InShipRankMapper {

    public ShipRank toProto(com.themonkeynauts.game.domain.ShipRank domain) {
        return domain.isRankB() ? ShipRank.B
                : domain.isRankA() ? ShipRank.A
                : domain.isRankS() ? ShipRank.S
                : ShipRank.SHIP_RANK_UNSPECIFIED;
    }

    public com.themonkeynauts.game.domain.ShipRank toDomain(ShipRank proto) {
        return ShipRank.B.equals(proto) ? new com.themonkeynauts.game.domain.ShipRank(ShipRankType.B)
                : ShipRank.A.equals(proto) ? new com.themonkeynauts.game.domain.ShipRank(ShipRankType.A)
                : ShipRank.S.equals(proto) ? new com.themonkeynauts.game.domain.ShipRank(ShipRankType.S)
                : new com.themonkeynauts.game.domain.ShipRank(null);
    }
}
