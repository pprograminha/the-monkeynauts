package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class AccountRepositoryIntegrationTest extends ApplicationContainerTest {

    @Test
    public void shouldSaveAccount() {
        var result = accountRepository.save(AccountEntity.builder().build());

        assertThat(result.getId()).isNotNull();
    }

}
