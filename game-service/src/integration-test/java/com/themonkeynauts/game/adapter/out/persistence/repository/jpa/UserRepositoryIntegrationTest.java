package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class UserRepositoryIntegrationTest extends ApplicationContainerTest {

    private AccountEntity account;
    private UserEntity player;
    private String email;
    private String nickname;

    @BeforeAll
    public void setUp() {
        var accountEntity = AccountEntity.builder().build();
        this.account = accountRepository.save(accountEntity);

        email = UUID.randomUUID().toString();
        nickname = UUID.randomUUID().toString();
        this.player = RepositoryHelper.newPlayer(account, email, nickname);
        userRepository.save(this.player);
    }

    @Test
    public void shouldFindByEmail() {
        var result = userRepository.findByEmail(email);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getRoles()).hasSize(2);
    }

    @Test
    public void shouldFindByNickname() {
        var result = userRepository.findByNickname(nickname);

        assertThat(result).isNotEmpty();
        assertThat(result.get().getRoles()).hasSize(2);
    }

    @Test
    public void shouldFindByEmailAndPassword() {
        var result = userRepository.findByEmailAndPassword(email, "password");

        assertThat(result).isNotEmpty();
        assertThat(result.get().getRoles()).hasSize(2);
    }
}
