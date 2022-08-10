package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautRankEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipRankEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class OutShipMapperTest {

    private OutShipMapper mapper;
    private OutUserMapper userMapper;

    private final AccountEntity accountEntity = new AccountEntity(UUID.randomUUID());

    @BeforeEach
    public void setUp() {
        userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        mapper = new OutShipMapper(userMapper, new OutMonkeynautMapper(userMapper));
    }

    @Test
    public void shouldConvertToEntity() {
        var shipId = UUID.randomUUID();
        var shipClass = new ShipClass(ShipClassType.MINER);
        var shipRank = new ShipRank(ShipRankType.S);
        var shipAttributes = new ShipAttributes(new ShipDurabilityAttribute(200));
        var ship = new Ship(shipId, "Name", shipClass, shipRank, shipAttributes);

        var monkeynautId = UUID.randomUUID();
        var monkeynautClass = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var monkeynautRank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);
        var monkeynautAttributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(8));
        var monkeynaut = new Monkeynaut(monkeynautId, "Firstname", "Lastname", monkeynautClass, monkeynautRank, monkeynautAttributes);

        ship.getCrew().addMonkeynaut(monkeynaut);

        var result = mapper.toEntity(ship);

        assertThat(result.getId()).isEqualTo(shipId);
        assertThat(result.getName()).isEqualTo("Name");
        assertThat(result.getClazz()).isEqualTo(ShipClassEnum.MINER);
        assertThat(result.getRank()).isEqualTo(ShipRankEnum.S);
        assertThat(result.getDurability()).isEqualTo(200);
        assertThat(result.getMonkeynauts()).hasSize(1);
        assertThat(result.getMonkeynauts().stream().findFirst().get().getId()).isEqualTo(monkeynautId);
    }

    @Test
    public void shouldConvertToDomain() {
        var userId = UUID.randomUUID();
        var userEntity = UserEntity.builder()
                .id(userId)
                .nickname("nickname")
                .email("email")
                .password("password")
                .roles(new HashSet<>(List.of(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var monkeynautId = UUID.randomUUID();
        var monkeynautEntity = MonkeynautEntity.builder()
                .id(monkeynautId)
                .firstName("Firstname")
                .lastName("Lastname")
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.CAPTAIN)
                .skill(50)
                .speed(50)
                .resistance(50)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .number(1L)
                .user(userEntity)
                .operator(userEntity)
                .account(accountEntity)
                .build();
        var shipId = UUID.randomUUID();
        var shipEntity = ShipEntity.builder()
                .id(shipId)
                .name("Name")
                .clazz(ShipClassEnum.MINER)
                .rank(ShipRankEnum.S)
                .durability(200)
                .monkeynauts(new HashSet<>(List.of(monkeynautEntity)))
                .account(accountEntity)
                .user(userEntity)
                .operator(userEntity)
                .build();

        var result = mapper.toDomain(shipEntity);

        assertThat(result.getId()).isEqualTo(shipId);
        assertThat(result.getName()).isEqualTo("Name");
        assertThat(result.getClazz()).isEqualTo(new ShipClass(ShipClassType.MINER));
        assertThat(result.getRank()).isEqualTo(new ShipRank(ShipRankType.S));
        assertThat(result.getAttributes().getCurrentDurability()).isEqualTo(new ShipDurabilityAttribute(200));
        assertThat(result.getCrew().monkeynauts().stream().findFirst().get().getId()).isEqualTo(monkeynautId);
        assertThat(result.getCrew().monkeynauts().stream().findFirst().get().getShipId()).isEqualTo(shipId);
    }

}