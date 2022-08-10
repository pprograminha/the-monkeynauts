package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateAccount;
import com.themonkeynauts.game.application.port.out.persistence.SaveAccountPort;
import com.themonkeynauts.game.domain.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    private CreateAccount createAccount;

    @Mock
    private SaveAccountPort saveAccount;

    @BeforeEach
    public void setUp() {
        createAccount = new CreateAccountUseCase(saveAccount);
    }

    @Test
    public void shouldCreateAccount() {
        var account = new Account();
        var accountId = UUID.randomUUID();
        var savedAccount = new Account(accountId);
        when(saveAccount.save(account)).thenReturn(savedAccount);

        var result = createAccount.create();

        assertThat(result).isEqualTo(savedAccount);
    }

}