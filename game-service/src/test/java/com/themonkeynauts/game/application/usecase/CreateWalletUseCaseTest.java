package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.in.CreateWallet;
import com.themonkeynauts.game.application.port.in.CreateWalletCommand;
import com.themonkeynauts.game.application.port.out.blockchain.WalletClientPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadWalletPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveWalletPort;
import com.themonkeynauts.game.common.exception.InvalidWalletAddressException;
import com.themonkeynauts.game.common.exception.UserAlreadyHasWalletException;
import com.themonkeynauts.game.common.exception.WalletAlreadyAssociatedException;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.User;
import com.themonkeynauts.game.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateWalletUseCaseTest {

    private CreateWallet createWallet;

    @Mock
    private LoadWalletPort loadWallet;

    @Mock
    private SaveWalletPort saveWallet;

    @Mock
    private WalletClientPort walletClient;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        createWallet = new CreateWalletUseCase(loadWallet, saveWallet, walletClient, securityUtils);
    }

    @Test
    public void shouldCreateWallet() {
        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "123456", "nickname", new Account());
        when(securityUtils.getAuthenticatedUser()).thenReturn(player);

        var walletId = UUID.randomUUID().toString();
        when(walletClient.isValidAddress(walletId)).thenReturn(true);

        var wallet = new Wallet(walletId, "BNB");
        when(saveWallet.save(wallet)).thenReturn(wallet);

        var command = new CreateWalletCommand(walletId, "BNB");
        var result = createWallet.create(command);

        assertThat(result).isEqualTo(wallet);
    }

    @Test
    public void shouldThrowExceptionIfPlayerAlreadyHasWallet() {
        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "123456", "nickname", new Account());
        var walletId = UUID.randomUUID();
        var walletAddress = UUID.randomUUID().toString();
        var wallet = new Wallet(walletId, walletAddress, "BNB 1");
        player.setWallet(wallet);
        when(securityUtils.getAuthenticatedUser()).thenReturn(player);

        assertThatThrownBy(() -> {
            var command = new CreateWalletCommand(UUID.randomUUID().toString(), "BNB 2");
            createWallet.create(command);
        })
        .isInstanceOf(UserAlreadyHasWalletException.class)
        .hasMessage("validation.user.already-has-wallet");
    }

    @Test
    public void shouldThrowExceptionIfWalletIsAlreadyAssociated() {
        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "123456", "nickname", new Account());
        when(securityUtils.getAuthenticatedUser()).thenReturn(player);

        var walletId = UUID.randomUUID().toString();
        when(loadWallet.existsByAddress(walletId)).thenReturn(true);

        assertThatThrownBy(() -> {
            var command = new CreateWalletCommand(walletId, "BNB");
            createWallet.create(command);
        })
        .isInstanceOf(WalletAlreadyAssociatedException.class)
        .hasMessage("validation.wallet.already-used");
    }

    @Test
    public void shouldThrowExceptionIfWalletHasInvalidAddress() {
        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "123456", "nickname", new Account());
        when(securityUtils.getAuthenticatedUser()).thenReturn(player);

        var walletId = UUID.randomUUID().toString();
        when(walletClient.isValidAddress(walletId)).thenReturn(false);

        assertThatThrownBy(() -> {
            var command = new CreateWalletCommand(walletId, "BNB");
            createWallet.create(command);
        })
        .isInstanceOf(InvalidWalletAddressException.class)
        .hasMessage("validation.wallet.invalid-address");
    }

}