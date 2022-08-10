package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateAccount;
import com.themonkeynauts.game.application.port.in.CreatePlayer;
import com.themonkeynauts.game.application.port.in.CreatePlayerCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadUserPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveUserPort;
import com.themonkeynauts.game.common.exception.CredentialsAlreadyTakenException;
import com.themonkeynauts.game.common.exception.CredentialsRequiredException;
import com.themonkeynauts.game.common.exception.InvalidFormatException;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePlayerUseCaseTest {

    private CreatePlayer createPlayer;

    @Mock
    private CreateAccount createAccount;

    @Mock
    private SaveUserPort saveUser;

    @Mock
    private LoadUserPort loadUser;

    @BeforeEach
    public void setUp() {
        createPlayer = new CreatePlayerUseCase(createAccount, saveUser, loadUser);
    }

    @Test
    public void shouldCreatePlayer() {
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        when(createAccount.create()).thenReturn(account);

        when(loadUser.byEmail("EMAIL@EMAIL.COM")).thenReturn(Optional.empty());
        when(loadUser.byNickname("NICK")).thenReturn(Optional.empty());

        var player = new User("EMAIL@EMAIL.COM", "NICK", "PASSWORD", account);
        player.addRole(Role.ADMIN);
        player.addRole(Role.PLAYER);

        var userId = UUID.randomUUID();
        var savedPlayer = new User(userId, "EMAIL@EMAIL.COM", "NICK", "PASSWORD", account);
        savedPlayer.addRole(Role.ADMIN);
        savedPlayer.addRole(Role.PLAYER);
        when(saveUser.save(player)).thenReturn(savedPlayer);

        var command = new CreatePlayerCommand("EMAIL@EMAIL.COM", "PASSWORD", "NICK");
        var result = createPlayer.create(command);

        assertThat(result).isEqualTo(savedPlayer);
    }

    @Test
    public void shouldThrowExceptionIfEmailIsNull() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand(null, "PASSWORD", "NICK")))
            .isInstanceOf(CredentialsRequiredException.class)
            .hasMessage("validation.required.credentials");
    }

    @Test
    public void shouldThrowExceptionIfEmailIsEmpty() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("", "PASSWORD", "")))
                .isInstanceOf(CredentialsRequiredException.class)
                .hasMessage("validation.required.credentials");
    }

    @Test
    public void shouldThrowExceptionIfPasswordIsNull() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL", null, "NICK")))
                .isInstanceOf(CredentialsRequiredException.class)
                .hasMessage("validation.required.credentials");
    }

    @Test
    public void shouldThrowExceptionIfPasswordIsEmpty() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL", "", "NICK")))
                .isInstanceOf(CredentialsRequiredException.class)
                .hasMessage("validation.required.credentials");
    }

    @Test
    public void shouldThrowExceptionIfNicknameIsNull() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL", "PASSWORD", null)))
                .isInstanceOf(CredentialsRequiredException.class)
                .hasMessage("validation.required.credentials");
    }

    @Test
    public void shouldThrowExceptionIfNicknameIsEmpty() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL", "PASSWORD", "")))
                .isInstanceOf(CredentialsRequiredException.class)
                .hasMessage("validation.required.credentials");
    }

    @Test
    public void shouldThrowExceptionIfEmailIsAlreadyTaken() {
        var account = new Account(UUID.randomUUID());
        when(loadUser.byEmail("EMAIL@EMAIL.COM")).thenReturn(Optional.of(new User(UUID.randomUUID(), "email", "nick", "PASSWORD", account)));

        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL@EMAIL.COM", "PASSWORD", "NICK")))
                .isInstanceOf(CredentialsAlreadyTakenException.class)
                .hasMessage("validation.duplicate.credentials");
    }

    @Test
    public void shouldThrowExceptionIfNicknameIsAlreadyTaken() {
        var account = new Account(UUID.randomUUID());
        when(loadUser.byNickname("NICK")).thenReturn(Optional.of(new User(UUID.randomUUID(), "email@email.com", "nick", "PASSWORD", account)));

        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL@EMAIL.COM", "PASSWORD", "NICK")))
                .isInstanceOf(CredentialsAlreadyTakenException.class)
                .hasMessage("validation.duplicate.credentials");
    }

    @Test
    public void shouldThrowExceptionIfEmailHasInvalidFormat() {
        assertThatThrownBy(() -> createPlayer.create(new CreatePlayerCommand("EMAIL", "PASSWORD", "NICK")))
                .isInstanceOf(InvalidFormatException.class)
                .hasMessage("validation.attribute.invalid");
    }

}