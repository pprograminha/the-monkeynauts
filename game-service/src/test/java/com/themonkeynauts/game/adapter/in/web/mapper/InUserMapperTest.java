package com.themonkeynauts.game.adapter.in.web.mapper;

import com.google.protobuf.Timestamp;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.proto.common.messages.Role;
import com.themonkeynauts.proto.common.messages.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class InUserMapperTest {

    @Test
    public void shouldConvertFromDomainToProto() {
        var id = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var domain = new com.themonkeynauts.game.domain.User(id, "EMAIL", "NICKNAME", "password", account);
        domain.addRole(com.themonkeynauts.game.domain.Role.ADMIN);
        domain.addRole(com.themonkeynauts.game.domain.Role.PLAYER);
        domain.setLastBountyHunting(LocalDateTime.of(2022, Month.JANUARY, 27, 14, 44, 57));

        var mapper = new InUserMapper(new InRoleMapper(), new InWalletMapper());
        var result = mapper.toProto(domain);

        var proto = User.newBuilder()
                .setId(id.toString())
                .setEmail("EMAIL")
                .setNickname("NICKNAME")
                .setLastBountyHuntingDateTime(Timestamp.newBuilder().setSeconds(1643294697).build())
                .addAllRoles(asList(Role.ADMIN, Role.PLAYER))
                .build();
        assertThat(result).isEqualTo(proto);
    }

}