package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.in.AuthenticateUserCommand;
import com.themonkeynauts.game.application.port.in.AuthenticationUser;
import com.themonkeynauts.game.application.port.out.persistence.LoadUserPort;
import com.themonkeynauts.game.application.port.out.security.TokenUtilsPort;
import com.themonkeynauts.game.common.exception.InvalidCredentialsException;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationUserUseCaseTest {

    @Mock
    private LoadUserPort loadUser;

    @Mock
    private TokenUtilsPort tokenUtils;

    @Mock
    private SecurityUtils securityUtils;

    private AuthenticationUser authenticationUser;

    @BeforeEach
    public void setUp() {
        authenticationUser = new AuthenticationUserUseCase(loadUser, tokenUtils, securityUtils);
    }

    @Test
    public void shouldAuthenticate() {
        var id = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var user = new User(id, "email", "nickname", "password", account);
        user.addRole(Role.ADMIN);
        user.addRole(Role.PLAYER);
        when(loadUser.byEmailAndPassword("email", "password")).thenReturn(Optional.of(user));

        var token = "token";
        var claims = new HashMap<String, String>();
        claims.put("userId", id.toString());
        claims.put("email", "email");
        claims.put("accountId", accountId.toString());
        claims.put("roles", "ADMIN,PLAYER");
        when(tokenUtils.generateToken("email", claims)).thenReturn(token);

        var command = new AuthenticateUserCommand("email", "password");
        var result = authenticationUser.login(command);

        assertThat(result).isEqualTo(token);
    }

    @Test
    public void shouldThrowExceptionForInvalidCredentials() {
        when(loadUser.byEmailAndPassword("email", "password")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            var command = new AuthenticateUserCommand("email", "password");
            authenticationUser.login(command);
        })
        .isInstanceOf(InvalidCredentialsException.class)
        .hasMessage("validation.authentication.invalid-credentials");
    }

    @Test
    public void shouldReturnAuthenticatedUser() {
        var id = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var user = new User(id, "email", "nickname", "password", account);
        user.addRole(Role.ADMIN);
        user.addRole(Role.PLAYER);
        when(securityUtils.getAuthenticatedUser()).thenReturn(user);

        var result = authenticationUser.authenticated();

        assertThat(result).isEqualTo(user);
    }

}