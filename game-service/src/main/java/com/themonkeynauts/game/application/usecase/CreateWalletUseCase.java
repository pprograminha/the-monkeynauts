package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.in.CreateWallet;
import com.themonkeynauts.game.application.port.in.CreateWalletCommand;
import com.themonkeynauts.game.application.port.out.blockchain.WalletClientPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadWalletPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveWalletPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.InvalidWalletAddressException;
import com.themonkeynauts.game.common.exception.UserAlreadyHasWalletException;
import com.themonkeynauts.game.common.exception.WalletAlreadyAssociatedException;
import com.themonkeynauts.game.domain.Wallet;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class CreateWalletUseCase implements CreateWallet {

    private final LoadWalletPort loadWallet;
    private final SaveWalletPort saveWallet;
    private final WalletClientPort walletClient;
    private final SecurityUtils securityUtils;

    @Override
    public Wallet create(CreateWalletCommand command) {
        var authenticatedUser = securityUtils.getAuthenticatedUser();
        if (authenticatedUser.hasWallet()) {
            throw new UserAlreadyHasWalletException();
        }

        var existsWallet = loadWallet.existsByAddress(command.address());
        if (existsWallet) {
            throw new WalletAlreadyAssociatedException();
        }

        var isValidWallet = walletClient.isValidAddress(command.address());
        if (!isValidWallet) {
            throw new InvalidWalletAddressException();
        }

        var wallet = new Wallet(command.address(), command.name());

        return saveWallet.save(wallet);
    }
}
