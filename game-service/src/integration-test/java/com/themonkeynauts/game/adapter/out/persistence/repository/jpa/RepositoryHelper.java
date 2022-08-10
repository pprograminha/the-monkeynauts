package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;

import java.util.LinkedHashSet;

import static java.util.Arrays.asList;

public class RepositoryHelper {

    public static UserEntity newPlayer(AccountEntity account, String email, String nickname) {
        var adminRole = RoleEntity.builder().roleId(RoleEnum.ADMIN).build();
        var playerRole = RoleEntity.builder().roleId(RoleEnum.PLAYER).build();
        return UserEntity.builder()
                .email(email)
                .password("password")
                .nickname(nickname)
                .roles(new LinkedHashSet<>(asList(adminRole, playerRole)))
                .account(account)
                .build();
    }

}
