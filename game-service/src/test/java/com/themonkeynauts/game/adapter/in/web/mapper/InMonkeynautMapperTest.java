package com.themonkeynauts.game.adapter.in.web.mapper;

import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRank;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
import com.themonkeynauts.game.domain.User;
import com.themonkeynauts.proto.common.messages.MonkeynautBonus;
import com.themonkeynauts.proto.common.messages.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InMonkeynautMapperTest {

    private InMonkeynautMapper mapper;

    @BeforeEach
    public void setUp() {
        var userMapper = new InUserMapper(new InRoleMapper(), new InWalletMapper());
        mapper = new InMonkeynautMapper(new InMonkeynautClassMapper(), new InMonkeynautRankMapper(), userMapper);
    }

    @Test
    public void shouldConvertMonkeynaut() {
        var id = UUID.randomUUID();
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var domain = new Monkeynaut(id, "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes);
        domain.setNumber(1L);
        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "nickname", "password", new Account(UUID.randomUUID()));
        domain.setPlayer(player);
        domain.setOperator(player);
        var shipId = UUID.randomUUID();
        domain.setShipId(shipId);

        var result = mapper.toProto(domain);

        var protoUser = com.themonkeynauts.proto.common.messages.User.newBuilder()
                .setId(userId.toString())
                .setEmail("email@email.com")
                .setNickname("nickname")
                .build();
        assertThat(result).isEqualTo(com.themonkeynauts.proto.common.messages.Monkeynaut.newBuilder()
                .setId(id.toString())
                .setNumber(1)
                .setFirstName("Firstname")
                .setLastName("Lastname")
                .setClass_(com.themonkeynauts.proto.common.messages.MonkeynautClass.ENGINEER)
                .setRank(com.themonkeynauts.proto.common.messages.MonkeynautRank.CAPTAIN)
                .setAttributes(com.themonkeynauts.proto.common.messages.MonkeynautAttributes.newBuilder().setSkill(75).setSpeed(60).setResistance(57).setLife(250).setCurrentEnergy(6).setMaxEnergy(6).build())
                .setShipId(shipId.toString())
                .setBonus(MonkeynautBonus.newBuilder().setDescription("Mining Reward").setValue(10f).build())
                .setOwner(protoUser)
                .setOperator(protoUser)
                .build());
    }

}