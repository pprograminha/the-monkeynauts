package com.themonkeynauts.game.adapter.out.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class UserEntityTest {

    @Test
    public void shouldBeEquals() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = AccountEntity.builder().id(accountId).build();
        var user1 = UserEntity.builder()
                .id(userId)
                .email("email")
                .nickname("nickname")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .account(account)
                .build();
        var user2 = UserEntity.builder()
                .id(userId)
                .email("email")
                .nickname("nickname")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .account(account)
                .build();

        assertThat(user1).isEqualTo(user2);
    }

}