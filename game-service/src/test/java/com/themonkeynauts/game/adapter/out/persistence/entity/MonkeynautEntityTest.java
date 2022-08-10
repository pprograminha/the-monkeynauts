package com.themonkeynauts.game.adapter.out.persistence.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MonkeynautEntityTest {

    @Test
    public void shouldBeEquals() {
        var monkeynautId = UUID.randomUUID();

        var userId = UUID.randomUUID();
        var user = UserEntity.builder().id(userId).build();

        var accountId = UUID.randomUUID();
        var account = AccountEntity.builder().id(accountId).build();

        var monkeynaut1 = MonkeynautEntity.builder()
                .id(monkeynautId)
                .firstName("Firstname")
                .lastName("Lastname")
                .clazz(MonkeynautClassEnum.SCIENTIST)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(user)
                .operator(user)
                .account(account)
                .build();
        var monkeynaut2 = MonkeynautEntity.builder()
                .id(monkeynautId)
                .firstName("Firstname")
                .lastName("Lastname")
                .clazz(MonkeynautClassEnum.SCIENTIST)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(user)
                .operator(user)
                .account(account)
                .build();

        assertThat(monkeynaut1).isEqualTo(monkeynaut2);
    }

}