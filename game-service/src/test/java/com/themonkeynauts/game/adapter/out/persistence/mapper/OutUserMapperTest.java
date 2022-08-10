package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class OutUserMapperTest {

    private OutUserMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
    }

    @Test
    public void shouldConvertFromDomainToEntity() {
        var id = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);

        var domain = new User(id, "EMAIL", "NICKNAME", "PASSWORD", account);
        var now = LocalDateTime.now();
        domain.setLastBountyHunting(now);
        domain.addRole(Role.ADMIN);
        var equipmentId = UUID.randomUUID();
        domain.addEquipment(new Equipment(equipmentId ,"Weapon #1", EquipmentType.WEAPON));

        var result = mapper.toEntity(domain, "PASSWORD");

        var roles = new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build()));
        var equipments = new HashSet<>(asList(EquipmentEntity.builder().id(equipmentId).name("Weapon #1").type(EquipmentEnum.WEAPON).build()));
        var accountEntity = AccountEntity.builder().id(accountId).build();
        var entity = UserEntity.builder().id(id).email("email").password("PASSWORD").nickname("nickname")
                .roles(roles)
                .equipments(equipments)
                .account(accountEntity)
                .lastBountyHunting(now)
                .build();
        assertThat(result).isEqualTo(entity);
    }

    @Test
    public void shouldConvertFromEntityToDomain() {
        var id = UUID.randomUUID();
        var adminRole = RoleEntity.builder().roleId(RoleEnum.ADMIN).build();
        var accountId = UUID.randomUUID();
        var accountEntity = AccountEntity.builder().id(accountId).build();
        var weaponId = UUID.randomUUID();
        var weapon = EquipmentEntity.builder().id(weaponId).name("Weapon #1").type(EquipmentEnum.WEAPON).build();
        var now = LocalDateTime.now();
        var entity = UserEntity.builder().id(id)
                .email("email").password("PASSWORD").nickname("nickname")
                .roles(new HashSet<>(asList(adminRole)))
                .equipments(new HashSet<>(asList(weapon)))
                .account(accountEntity)
                .lastBountyHunting(now)
                .build();

        var result = mapper.toDomain(entity);

        var account = new Account(accountId);
        var player = new User(id, "email", "nickname", "PASSWORD", account);
        player.setLastBountyHunting(now);
        player.addRole(Role.ADMIN);
        player.addEquipment(new Equipment(weaponId, "Weapon #1", EquipmentType.WEAPON));
        assertThat(result).isEqualTo(player);
    }

}