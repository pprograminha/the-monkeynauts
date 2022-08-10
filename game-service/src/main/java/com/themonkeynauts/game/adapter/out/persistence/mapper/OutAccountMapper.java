package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Account;

@MapperAdapter
public class OutAccountMapper {

    public Account toDomain(AccountEntity entity) {
        return new Account(entity.getId());
    }

    public AccountEntity toEntity(Account account) {
        return AccountEntity.builder().id(account.getId()).build();
    }
}
