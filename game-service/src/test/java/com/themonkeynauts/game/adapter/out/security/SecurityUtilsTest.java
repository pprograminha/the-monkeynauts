package com.themonkeynauts.game.adapter.out.security;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutEquipmentMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutWalletMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityUtilsTest {

    private SecurityUtils securityUtils;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext context;

    @Mock
    private Authentication authentication;

    @Mock
    private Jwt jwt;

    private Account account = new Account(UUID.randomUUID());

    @BeforeEach
    public void setUp() {
        var userMapper = new OutUserMapper(new OutAccountMapper(), new OutEquipmentMapper(), new OutWalletMapper());
        securityUtils = new SecurityUtils(userRepository, userMapper);
    }

    @Test
    public void shouldGetAuthenticatedUser() {
        SecurityContextHolder.setContext(context);

        when(context.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        var claims = new HashMap<String, Object>();
        var userId = UUID.randomUUID();
        claims.put("userId", userId.toString());
        when(jwt.getClaims()).thenReturn(claims);

        var accountEntity = AccountEntity.builder().id(account.getId()).build();
        var userEntity = UserEntity.builder().id(userId)
                .email("email").password("password").nickname("nickname")
                .account(accountEntity)
                .roles(new HashSet<>(asList(RoleEntity.builder().roleId(RoleEnum.ADMIN).build())))
                .equipments(new HashSet<>())
                .build();
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        var result = securityUtils.getAuthenticatedUser();
        var user = new User(userId, "email", "nickname", "password", account);
        user.addRole(Role.ADMIN);

        assertThat(result).isEqualTo(user);
    }

}