package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.WalletEntity;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Wallet;

import java.math.BigDecimal;

@MapperAdapter
public class OutWalletMapper {

    public WalletEntity toEntity(Wallet domain) {
        return WalletEntity.builder()
                .id(domain.getId())
                .address(domain.getAddress())
                .name(domain.getName())
                .balance(domain.getBalance().getAmount().doubleValue())
                .build();
    }

    public Wallet toDomain(WalletEntity entity) {
        var wallet = new Wallet(entity.getId(), entity.getAddress(), entity.getName());
        wallet.add(new BigDecimal(entity.getBalance()));
        return wallet;
    }
}
