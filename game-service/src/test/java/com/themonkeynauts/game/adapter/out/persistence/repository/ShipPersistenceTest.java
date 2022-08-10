package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipRankEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutEquipmentMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutMonkeynautMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutShipMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutWalletMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.ShipRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import com.themonkeynauts.game.domain.ShipRankType;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipPersistenceTest {

    private ShipPersistence persistence;

    @Mock
    private ShipRepository repository;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        var userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        var shipMapper = new OutShipMapper(userMapper, new OutMonkeynautMapper(userMapper));
        persistence = new ShipPersistence(repository, userMapper, shipMapper, securityUtils);
    }

    @Test
    public void shouldSaveShipWithSpecificUser() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();

        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId)
                .email("email").nickname("nickname").password("password")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var entity = ShipEntity.builder()
                .name("Europa")
                .clazz(ShipClassEnum.MINER)
                .rank(ShipRankEnum.A)
                .user(userEntity)
                .operator(userEntity)
                .durability(50)
                .monkeynauts(new HashSet<>())
                .build();
        var id = UUID.randomUUID();
        var savedEntity = ShipEntity.builder()
                .id(id)
                .name("Europa")
                .clazz(ShipClassEnum.MINER)
                .rank(ShipRankEnum.A)
                .user(userEntity)
                .operator(userEntity)
                .durability(50)
                .monkeynauts(new HashSet<>())
                .build();
        when(repository.save(entity)).thenReturn(savedEntity);

        var account = new Account(accountId);
        var player = new User(userId, "email", "nickname", "password", account);
        var attributes = new ShipAttributes(new ShipDurabilityAttribute(50));
        var ship = new Ship("Europa", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.A), attributes);
        ship.setOwner(player);
        ship.setOperator(player);
        var result = persistence.save(ship);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Europa");
        assertThat(result.getClazz()).isEqualTo(new ShipClass(ShipClassType.MINER));
        assertThat(result.getRank()).isEqualTo(new ShipRank(ShipRankType.A));
        assertThat(result.getOwner().getId()).isEqualTo(userId);
        assertThat(result.getOperator().getId()).isEqualTo(userId);
    }

    @Test
    public void shouldSaveShipWithAuthenticatedUser() {
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
        var entity = ShipEntity.builder()
                .name("Europa")
                .clazz(ShipClassEnum.MINER)
                .rank(ShipRankEnum.A)
                .durability(50)
                .monkeynauts(new HashSet<>())
                .build();
        var id = UUID.randomUUID();
        var savedEntity = ShipEntity.builder()
                .id(id)
                .name("Europa")
                .clazz(ShipClassEnum.MINER)
                .rank(ShipRankEnum.A)
                .user(userEntity)
                .operator(userEntity)
                .durability(50)
                .monkeynauts(new HashSet<>())
                .build();
        when(repository.save(entity)).thenReturn(savedEntity);

        var attributes = new ShipAttributes(new ShipDurabilityAttribute(50));
        var ship = new Ship("Europa", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.A), attributes);
        var result = persistence.save(ship);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Europa");
        assertThat(result.getClazz()).isEqualTo(new ShipClass(ShipClassType.MINER));
        assertThat(result.getRank()).isEqualTo(new ShipRank(ShipRankType.A));
        assertThat(result.getOwner().getId()).isEqualTo(userId);
        assertThat(result.getOperator().getId()).isEqualTo(userId);
    }

    @Test
    public void shouldListAllShips() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId).email("email").nickname("nickname")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var id = UUID.randomUUID();
        var entity = ShipEntity.builder()
                .id(id)
                .name("Firstname")
                .clazz(ShipClassEnum.EXPLORER)
                .rank(ShipRankEnum.B)
                .user(userEntity)
                .operator(userEntity)
                .durability(4)
                .monkeynauts(new HashSet<>())
                .build();
        when(repository.findAll()).thenReturn(asList(entity));

        var result = persistence.all();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(id);
    }

    @Test
    public void shouldFindShipById() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder().id(userId).email("email").nickname("nickname")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(accountEntity)
                .build();
        var id = UUID.randomUUID();
        var entity = ShipEntity.builder()
                .id(id)
                .name("Firstname")
                .clazz(ShipClassEnum.EXPLORER)
                .rank(ShipRankEnum.B)
                .user(userEntity)
                .operator(userEntity)
                .durability(4)
                .monkeynauts(new HashSet<>())
                .build();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        var result = persistence.byId(id);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(id);
    }

    @Test
    public void shouldReturnEmptyInFindShipById() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        var result = persistence.byId(id);

        assertThat(result).isEmpty();
    }

}