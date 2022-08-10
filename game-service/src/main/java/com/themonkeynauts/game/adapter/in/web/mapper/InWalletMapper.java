package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.proto.common.messages.Wallet;

@MapperAdapter
public class InWalletMapper {

    public Wallet toProto(com.themonkeynauts.game.domain.Wallet domain) {
        return Wallet.newBuilder()
                .setId(domain.getId().toString())
                .setAddress(domain.getAddress())
                .setName(domain.getName())
                .setBalance(domain.getBalance().getAmount().floatValue())
                .build();
    }

}
