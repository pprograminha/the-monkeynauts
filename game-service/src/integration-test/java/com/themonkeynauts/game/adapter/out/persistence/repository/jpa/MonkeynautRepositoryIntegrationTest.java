package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautRankEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.domain.Account;
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
public class MonkeynautRepositoryIntegrationTest extends ApplicationContainerTest {

    private AccountEntity account;
    private UserEntity player;
    private String email;
    private String nickname;

    @BeforeAll
    public void setUp() {
        var accountEntity = AccountEntity.builder().build();
        this.account = accountRepository.save(accountEntity);

        securityUtils.setTenant(new Account(accountEntity.getId()));

        email = UUID.randomUUID().toString();
        nickname = UUID.randomUUID().toString();
        this.player = RepositoryHelper.newPlayer(account, email, nickname);
        userRepository.save(this.player);
    }

    @Test
    public void shouldFindAllMonkeynauts() {
        var monkeynaut = MonkeynautEntity.builder()
                .number(1L)
                .firstName("Firstname")
                .lastName("Lastname")
                .clazz(MonkeynautClassEnum.ENGINEER)
                .rank(MonkeynautRankEnum.MAJOR)
                .user(this.player)
                .operator(this.player)
                .account(account)
                .skill(50)
                .speed(50)
                .resistance(50)
                .life(350)
                .currentEnergy(8)
                .maxEnergy(8)
                .build();
        monkeynautRepository.save(monkeynaut);

        var foundMonkeynauts = monkeynautRepository.findAll();
        
        assertThat(foundMonkeynauts).hasSize(1);
        assertThat(foundMonkeynauts.get(0).getNumber()).isEqualTo(1L);
        assertThat(foundMonkeynauts.get(0).getClazz()).isEqualTo(MonkeynautClassEnum.ENGINEER);
        assertThat(foundMonkeynauts.get(0).getClazz()).isEqualTo(MonkeynautClassEnum.ENGINEER);
        assertThat(foundMonkeynauts.get(0).getRank()).isEqualTo(MonkeynautRankEnum.MAJOR);
        assertThat(foundMonkeynauts.get(0).getUser().getId()).isEqualTo(this.player.getId());
        assertThat(foundMonkeynauts.get(0).getOperator().getId()).isEqualTo(this.player.getId());
        assertThat(foundMonkeynauts.get(0).getSkill()).isEqualTo(50);
        assertThat(foundMonkeynauts.get(0).getSpeed()).isEqualTo(50);
        assertThat(foundMonkeynauts.get(0).getResistance()).isEqualTo(50);
        assertThat(foundMonkeynauts.get(0).getLife()).isEqualTo(350);
        assertThat(foundMonkeynauts.get(0).getCurrentEnergy()).isEqualTo(8);
        assertThat(foundMonkeynauts.get(0).getMaxEnergy()).isEqualTo(8);
    }

}
