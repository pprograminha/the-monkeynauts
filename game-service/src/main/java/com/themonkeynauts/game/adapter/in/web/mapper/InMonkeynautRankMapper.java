package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.proto.common.messages.MonkeynautRank;

@MapperAdapter
public class InMonkeynautRankMapper {

    public com.themonkeynauts.game.domain.MonkeynautRank toDomain(MonkeynautRank proto) {
        return MonkeynautRank.PRIVATE.equals(proto) ? new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.PRIVATE)
                : MonkeynautRank.SERGEANT.equals(proto) ? new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.SERGEANT)
                : MonkeynautRank.CAPTAIN.equals(proto) ? new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.CAPTAIN)
                : MonkeynautRank.MAJOR.equals(proto) ? new com.themonkeynauts.game.domain.MonkeynautRank(MonkeynautRankType.MAJOR)
                : new com.themonkeynauts.game.domain.MonkeynautRank(null);
    }

    public MonkeynautRank toProto(com.themonkeynauts.game.domain.MonkeynautRank rank) {
        return rank.isPrivate() ? MonkeynautRank.PRIVATE
                : rank.isSergeant() ? MonkeynautRank.SERGEANT
                : rank.isCaptain() ? MonkeynautRank.CAPTAIN
                : rank.isMajor() ? MonkeynautRank.MAJOR
                : MonkeynautRank.MONKEYNAUT_RANK_UNSPECIFIED;
    }
}
