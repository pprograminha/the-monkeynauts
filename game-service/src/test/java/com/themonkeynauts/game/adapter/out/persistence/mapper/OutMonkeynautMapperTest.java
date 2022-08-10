package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautRankEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRank;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class OutMonkeynautMapperTest {

    private OutMonkeynautMapper mapper;

    @BeforeEach
    public void setUp() {
        var userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        mapper = new OutMonkeynautMapper(userMapper);
    }

    @Test
    public void shouldConvertToEntity() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SOLDIER);
        var rank = new MonkeynautRank(MonkeynautRankType.SERGEANT);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(30), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var domain = new Monkeynaut(id, "Firstname", "Lastname", clazz, rank, attributes);
        domain.setNumber(1L);

        var result = mapper.toEntity(domain);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getFirstName()).isEqualTo("Firstname");
        assertThat(result.getLastName()).isEqualTo("Lastname");
        assertThat(result.getClazz()).isEqualTo(MonkeynautClassEnum.SOLDIER);
        assertThat(result.getRank()).isEqualTo(MonkeynautRankEnum.SERGEANT);
        assertThat(result.getSkill()).isEqualTo(30);
        assertThat(result.getSpeed()).isEqualTo(40);
        assertThat(result.getResistance()).isEqualTo(50);
        assertThat(result.getLife()).isEqualTo(250);
        assertThat(result.getCurrentEnergy()).isEqualTo(4);
    }

    @Test
    public void shouldConvertToDomain() {
        var id = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = AccountEntity.builder().id(accountId).build();
        var player = UserEntity.builder()
                .email("email")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.PLAYER).build())))
                .equipments(new HashSet<>())
                .account(account)
                .build();
        var entity = MonkeynautEntity.builder()
                .id(id)
                .number(1L)
                .firstName("Firstname")
                .lastName("Lastname")
                .clazz(MonkeynautClassEnum.SCIENTIST)
                .rank(MonkeynautRankEnum.CAPTAIN)
                .user(player)
                .operator(player)
                .skill(30)
                .speed(40)
                .resistance(50)
                .life(250)
                .currentEnergy(6)
                .maxEnergy(6)
                .build();

        var result = mapper.toDomain(entity);

        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNumber()).isEqualTo(1);
        assertThat(result.getFirstName()).isEqualTo("Firstname");
        assertThat(result.getLastName()).isEqualTo("Lastname");
        assertThat(result.getClazz()).isEqualTo(clazz);
        assertThat(result.getRank()).isEqualTo(rank);
        assertThat(result.getPlayer().getEmail()).isEqualTo("email");
        assertThat(result.getOperator().getEmail()).isEqualTo("email");
        assertThat(result.getAttributes().getSkill().getValue()).isEqualTo(30);
        assertThat(result.getAttributes().getSpeed().getValue()).isEqualTo(40);
        assertThat(result.getAttributes().getResistance().getValue()).isEqualTo(50);
        assertThat(result.getAttributes().getLife().getValue()).isEqualTo(250);
        assertThat(result.getAttributes().getCurrentEnergy().getValue()).isEqualTo(6);
    }

}