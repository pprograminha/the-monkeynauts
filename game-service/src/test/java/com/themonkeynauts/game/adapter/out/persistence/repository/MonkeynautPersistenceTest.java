package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautRankEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutEquipmentMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutMonkeynautMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutWalletMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.MonkeynautNumberRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.MonkeynautRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
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
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonkeynautPersistenceTest {

    private MonkeynautPersistence persistence;

    @Mock
    private MonkeynautRepository monkeynautRepository;

    @Mock
    private MonkeynautNumberRepository monkeynautNumberRepository;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        var userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        persistence = new MonkeynautPersistence(new OutMonkeynautMapper(userMapper), securityUtils, monkeynautRepository, monkeynautNumberRepository);
    }

    @Test
    public void shouldSaveMonkeynautWithSpecificUser() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId)
                .email("email").nickname("nickname").password("password")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var entity = MonkeynautEntity.builder()
                .firstName("Firstname").lastName("Lastname")
                .number(1L)
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(userEntity)
                .operator(userEntity)
                .skill(50)
                .speed(40)
                .resistance(30)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .build();
        var id = UUID.randomUUID();
        var savedEntity = MonkeynautEntity.builder()
                .id(id)
                .number(1L)
                .firstName("Firstname").lastName("Lastname")
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(userEntity)
                .operator(userEntity)
                .skill(50)
                .speed(40)
                .resistance(30)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .build();
        when(monkeynautRepository.save(entity)).thenReturn(savedEntity);

        when(monkeynautNumberRepository.getLastNumber()).thenReturn(0L);

        var account = new Account(accountId);
        var player = new User(id, "email", "nickname", "password", account);
        player.addRole(Role.ADMIN);
        player.addRole(Role.PLAYER);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var monkeynaut = new Monkeynaut("Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.SERGEANT), attributes);
        monkeynaut.setPlayer(player);
        monkeynaut.setOperator(player);

        var result = persistence.save(monkeynaut);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNumber()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Firstname");
        assertThat(result.getLastName()).isEqualTo("Lastname");
        assertThat(result.getClazz()).isEqualTo(new MonkeynautClass(MonkeynautClassType.ENGINEER));
        assertThat(result.getRank()).isEqualTo(new MonkeynautRank(MonkeynautRankType.SERGEANT));
        assertThat(result.getPlayer().getId()).isEqualTo(userId);
        assertThat(result.getOperator().getId()).isEqualTo(userId);
        assertThat(result.getAttributes().getSkill().getValue()).isEqualTo(50);
        assertThat(result.getAttributes().getSpeed().getValue()).isEqualTo(40);
        assertThat(result.getAttributes().getResistance().getValue()).isEqualTo(30);
        assertThat(result.getAttributes().getLife().getValue()).isEqualTo(250);
        assertThat(result.getAttributes().getCurrentEnergy().getValue()).isEqualTo(4);

        verify(monkeynautNumberRepository).incrementNumber();
    }

    @Test
    public void shouldSaveMonkeynautWithAuthenticatedUser() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var user = new User(userId, "email", "nickname", "password", account);
        user.addRole(Role.ADMIN);
        when(securityUtils.getAuthenticatedUser()).thenReturn(user);

        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId)
                .email("email").nickname("nickname").password("password")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var entity = MonkeynautEntity.builder()
                .firstName("Firstname").lastName("Lastname")
                .number(1L)
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.SERGEANT)
                .skill(50)
                .speed(40)
                .resistance(30)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .build();
        var id = UUID.randomUUID();
        var savedEntity = MonkeynautEntity.builder()
                .id(id)
                .number(1L)
                .firstName("Firstname").lastName("Lastname")
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(userEntity)
                .operator(userEntity)
                .skill(50)
                .speed(40)
                .resistance(30)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .build();
        when(monkeynautRepository.save(entity)).thenReturn(savedEntity);

        when(monkeynautNumberRepository.getLastNumber()).thenReturn(0L);

        var player = new User(id, "email", "nickname", "password", account);
        player.addRole(Role.ADMIN);
        player.addRole(Role.PLAYER);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var monkeynaut = new Monkeynaut("Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.SERGEANT), attributes);

        var result = persistence.save(monkeynaut);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getNumber()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("Firstname");
        assertThat(result.getLastName()).isEqualTo("Lastname");
        assertThat(result.getClazz()).isEqualTo(new MonkeynautClass(MonkeynautClassType.ENGINEER));
        assertThat(result.getRank()).isEqualTo(new MonkeynautRank(MonkeynautRankType.SERGEANT));
        assertThat(result.getPlayer().getId()).isEqualTo(userId);
        assertThat(result.getOperator().getId()).isEqualTo(userId);
        assertThat(result.getAttributes().getSkill().getValue()).isEqualTo(50);
        assertThat(result.getAttributes().getSpeed().getValue()).isEqualTo(40);
        assertThat(result.getAttributes().getResistance().getValue()).isEqualTo(30);
        assertThat(result.getAttributes().getLife().getValue()).isEqualTo(250);
        assertThat(result.getAttributes().getCurrentEnergy().getValue()).isEqualTo(4);

        verify(monkeynautNumberRepository).incrementNumber();
    }

    @Test
    public void shouldListAllMonkeynauts() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId).email("email").nickname("nickname")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var id = UUID.randomUUID();
        var entity = MonkeynautEntity.builder()
                .id(id)
                .firstName("Firstname").lastName("Lastname")
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(userEntity)
                .operator(userEntity)
                .skill(50)
                .speed(40)
                .resistance(30)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .build();
        when(monkeynautRepository.findAllByOrderByNumberAsc()).thenReturn(asList(entity));

        var result = persistence.all();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(id);
    }

    @Test
    public void shouldFindMonkeynautById() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId).email("email").nickname("nickname")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var id = UUID.randomUUID();
        var entity = MonkeynautEntity.builder()
                .id(id)
                .firstName("Firstname").lastName("Lastname")
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.SERGEANT)
                .user(userEntity)
                .operator(userEntity)
                .skill(50)
                .speed(40)
                .resistance(30)
                .life(250)
                .currentEnergy(4)
                .maxEnergy(4)
                .build();
        when(monkeynautRepository.findById(id)).thenReturn(Optional.of(entity));

        var result = persistence.byId(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    public void shouldReturnEmptyInFindMonkeynautById() {
        var id = UUID.randomUUID();
        when(monkeynautRepository.findById(id)).thenReturn(Optional.empty());

        var result = persistence.byId(id);

        assertThat(result).isEmpty();
    }

}