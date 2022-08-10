package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateAccount;
import com.themonkeynauts.game.application.port.out.persistence.SaveAccountPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.domain.Account;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class CreateAccountUseCase implements CreateAccount {

    private final SaveAccountPort saveAccount;

    @Override
    public Account create() {
        return saveAccount.save(new Account());
    }
}
