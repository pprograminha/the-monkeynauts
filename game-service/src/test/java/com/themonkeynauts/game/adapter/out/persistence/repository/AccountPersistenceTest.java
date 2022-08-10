package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.AccountRepository;
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
class AccountPersistenceTest {

    private AccountPersistence persistence;

    @Mock
    private AccountRepository repository;

    @BeforeEach
    public void setUp() {
        persistence = new AccountPersistence(new OutAccountMapper(), repository);
    }

    @Test
    public void shouldSaveAccount() {
        var entity = AccountEntity.builder().build();
        var accountId = UUID.randomUUID();
        var savedEntity = AccountEntity.builder().id(accountId).build();
        when(repository.save(entity)).thenReturn(savedEntity);

        var account = new Account();
        var result = persistence.save(account);

        assertThat(result).isEqualTo(new Account(accountId));
    }

}