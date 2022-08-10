package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class InRoleEntityMapperTest {

    private InRoleMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new InRoleMapper();
    }

    @Test
    public void shouldConvertToProto() {
        var player = Role.PLAYER;
        var admin = Role.ADMIN;
        var domains = asList(player, admin);

        var result = domains.stream().map(mapper::toProto).toList();

        assertThat(result).containsExactly(com.themonkeynauts.proto.common.messages.Role.PLAYER, com.themonkeynauts.proto.common.messages.Role.ADMIN);
    }

}