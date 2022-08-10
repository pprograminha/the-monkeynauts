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
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import com.themonkeynauts.game.domain.ShipRankType;
import com.themonkeynauts.game.domain.User;
import com.themonkeynauts.proto.common.messages.MonkeynautBonus;
import com.themonkeynauts.proto.common.messages.ShipBonus;
import com.themonkeynauts.proto.common.messages.ShipCrew;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class InShipMapperTest {

    private InShipMapper mapper;

    @BeforeEach
    public void setUp() {
        var userMapper = new InUserMapper(new InRoleMapper(), new InWalletMapper());
        var monkeynautMapper = new InMonkeynautMapper(new InMonkeynautClassMapper(), new InMonkeynautRankMapper(), userMapper);
        mapper = new InShipMapper(new InShipClassMapper(), new InShipRankMapper(), monkeynautMapper, userMapper);
    }

    @Test
    public void shouldConvertToProto() {
        var monkeynautId = UUID.randomUUID();
        var monkeynautAttributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var monkeynaut = new Monkeynaut(monkeynautId, "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), monkeynautAttributes);
        monkeynaut.setNumber(1L);
        var shipId = UUID.randomUUID();
        var shipAttributes = new ShipAttributes(new ShipDurabilityAttribute(100));
        var ship = new Ship(shipId, "Name", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.S), shipAttributes);
        ship.getCrew().addMonkeynaut(monkeynaut);
        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "nickname", "password", new Account(UUID.randomUUID()));
        ship.setOwner(player);
        ship.setOperator(player);

        var result = mapper.toProto(ship);

        var protoUser = com.themonkeynauts.proto.common.messages.User.newBuilder()
                .setId(userId.toString())
                .setEmail("email@email.com")
                .setNickname("nickname")
                .build();
        var protoCrew = ShipCrew.newBuilder()
                .addMonkeynauts(com.themonkeynauts.proto.common.messages.Monkeynaut.newBuilder()
                        .setId(monkeynautId.toString())
                        .setFirstName("Firstname")
                        .setLastName("Lastname")
                        .setClass_(com.themonkeynauts.proto.common.messages.MonkeynautClass.ENGINEER)
                        .setRank(com.themonkeynauts.proto.common.messages.MonkeynautRank.CAPTAIN)
                        .setAttributes(com.themonkeynauts.proto.common.messages.MonkeynautAttributes.newBuilder().setSkill(75).setSpeed(60).setResistance(57).setLife(250).setCurrentEnergy(6).setMaxEnergy(6).build())
                        .setNumber(1L)
                        .setShipId(shipId.toString())
                        .setBonus(MonkeynautBonus.newBuilder().setDescription("Mining Reward").setValue(10f).build())
                        .build())
                .setSeats(4)
                .build();
        assertThat(result).isEqualTo(com.themonkeynauts.proto.common.messages.Ship.newBuilder()
                .setId(shipId.toString())
                .setName("Name")
                .setClass_(com.themonkeynauts.proto.common.messages.ShipClass.MINER)
                .setRank(com.themonkeynauts.proto.common.messages.ShipRank.S)
                .setAttributes(com.themonkeynauts.proto.common.messages.ShipAttributes.newBuilder().setCurrentDurability(100).setMaxDurability(400).build())
                .setBonus(ShipBonus.newBuilder().setDescription("Mining Mission Success Rate").setValue(40f).build())
                .setCrew(protoCrew)
                .setOwner(protoUser)
                .setOperator(protoUser)
                .build());
    }

}