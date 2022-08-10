package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutEquipmentMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutWalletMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserPersistenceTest {

    private UserPersistence persistence;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @BeforeEach
    public void setUp() {
        var userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        persistence = new UserPersistence(userRepository, encoder, userMapper);
    }

    @Test
    public void shouldReturnEmptyWhenEmailAndPasswordDontExist() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        var result = persistence.byEmailAndPassword("EMAIL", "password");

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnEmptyWhenEmailAndPasswordDontMatch() {
        when(encoder.matches("password", "drowssap")).thenReturn(false);
        var entity = UserEntity.builder().password("drowssap").build();
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(entity));

        var result = persistence.byEmailAndPassword("EMAIL", "password");

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnPresentWhenEmailAndPasswordMatch() {
        when(encoder.matches("password", "drowssap")).thenReturn(true);
        var account = AccountEntity.builder().id(UUID.randomUUID()).build();
        var entity = UserEntity.builder().password("drowssap")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(account)
                .build();
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(entity));

        var result = persistence.byEmailAndPassword("EMAIL", "password");

        assertThat(result).isPresent();
    }

    @Test
    public void shouldReturnPresentIfEmailExists() {
        var account = AccountEntity.builder().id(UUID.randomUUID()).build();
        var entity = UserEntity.builder().password("drowssap")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(account)
                .build();
        when(userRepository.findByEmail("email")).thenReturn(Optional.of(entity));

        var result = persistence.byEmail("EMAIL");

        assertThat(result).isPresent();
    }

    @Test
    public void shouldReturnEmptyIfEmailDoesNotExist() {
        when(userRepository.findByEmail("email")).thenReturn(Optional.empty());

        var result = persistence.byEmail("EMAIL");

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldReturnPresentIfNicknameExists() {
        var account = AccountEntity.builder().id(UUID.randomUUID()).build();
        var entity = UserEntity.builder().password("drowssap")
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .account(account)
                .build();
        when(userRepository.findByNickname("nickname")).thenReturn(Optional.of(entity));

        var result = persistence.byNickname("NICKNAME");

        assertThat(result).isPresent();
    }

    @Test
    public void shouldReturnEmptyIfNicknameDoesNotExist() {
        when(userRepository.findByNickname("nickname")).thenReturn(Optional.empty());

        var result = persistence.byNickname("NICKNAME");

        assertThat(result).isEmpty();
    }

    @Test
    public void shouldStartBountyHunting() {
        var userId = UUID.randomUUID();
        var time = LocalDateTime.now();
        persistence.startBountyHuntingQuest(userId, time);

        verify(userRepository).startBountyHunting(userId, time);
    }

}