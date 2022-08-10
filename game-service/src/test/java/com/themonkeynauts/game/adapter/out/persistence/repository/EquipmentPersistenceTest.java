package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutEquipmentMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutWalletMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.EquipmentRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;
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
class EquipmentPersistenceTest {

    private EquipmentPersistence persistence;

    @Mock
    private SecurityUtils securityUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @BeforeEach
    public void setUp() {
        var userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        persistence = new EquipmentPersistence(securityUtils, userRepository, equipmentRepository, userMapper);
    }

    @Test
    public void shouldReturnEmptyIfEquipmentDoesNotExist() {
        var equipmentId = UUID.randomUUID();
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.empty());

        var result = persistence.byId(equipmentId);

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEquipmentWhenExistsById() {
        var equipmentId = UUID.randomUUID();
        var equipmentEntity = EquipmentEntity.builder().id(equipmentId).name("Weapon #1").type(EquipmentEnum.WEAPON).build();
        when(equipmentRepository.findById(equipmentId)).thenReturn(Optional.of(equipmentEntity));

        var result = persistence.byId(equipmentId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(new Equipment(equipmentId, "Weapon #1", EquipmentType.WEAPON));
    }

    @Test
    public void shouldGrantEquipmentToAuthenticatedUser() {
        var userId = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var user = new User(userId, "email", "nickname", "password", account);
        user.addRole(Role.ADMIN);
        when(securityUtils.getAuthenticatedUser()).thenReturn(user);

        var equipmentId = UUID.randomUUID();
        var equipment = new Equipment(equipmentId, "Weapon #1", EquipmentType.WEAPON);

        persistence.grant(equipment);

        var accountEntity = AccountEntity.builder().id(accountId).build();
        var userEntity = UserEntity.builder()
                .id(userId)
                .account(accountEntity)
                .email("email")
                .nickname("nickname")
                .password("password")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>(asList(EquipmentEntity.builder().id(equipmentId).name("Weapon #1").type(EquipmentEnum.WEAPON).build())))
                .build();
        verify(userRepository).save(userEntity);
    }

}