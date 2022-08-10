package com.themonkeynauts.game.adapter.in.web.mapper;

import com.google.protobuf.Timestamp;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;

import java.time.ZoneOffset;

@MapperAdapter
@RequiredArgsConstructor
public class InUserMapper {

    private final InRoleMapper roleMapper;
    private final InWalletMapper walletMapper;

    public com.themonkeynauts.proto.common.messages.User toProto(User domain) {
        var roles = domain.getRoles().stream().map(roleMapper::toProto).toList();
        var protoBuilder = com.themonkeynauts.proto.common.messages.User.newBuilder()
                .setId(domain.getId().toString())
                .setEmail(domain.getEmail())
                .setNickname(domain.getNickname())
                .addAllRoles(roles);
        if (domain.getLastBountyHunting() != null) {
            var lastBountyHuntingInstant = domain.getLastBountyHunting().toInstant(ZoneOffset.UTC);
            var lastBountyHuntingTimestamp = Timestamp.newBuilder().setSeconds(lastBountyHuntingInstant.getEpochSecond()).setNanos(lastBountyHuntingInstant.getNano()).build();
            protoBuilder.setLastBountyHuntingDateTime(lastBountyHuntingTimestamp);
        }
        if (domain.hasWallet()) {
            var wallet = walletMapper.toProto(domain.getWallet());
            protoBuilder.setWallet(wallet);
        }
        return protoBuilder.build();
    }
}
