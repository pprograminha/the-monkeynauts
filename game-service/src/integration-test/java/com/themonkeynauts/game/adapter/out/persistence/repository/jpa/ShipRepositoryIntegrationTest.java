package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.game.adapter.out.persistence.entity.AccountEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipClassEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.ShipRankEnum;
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
public class ShipRepositoryIntegrationTest extends ApplicationContainerTest {

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
    public void shouldFindAllShips() {
        var ship = ShipEntity.builder()
                .name("Name")
                .user(player)
                .operator(player)
                .account(account)
                .durability(400)
                .clazz(ShipClassEnum.MINER)
                .rank(ShipRankEnum.S)
                .build();

        shipRepository.save(ship);

        var foundShips = shipRepository.findAll();

        assertThat(foundShips).hasSize(1);
        assertThat(foundShips.get(0).getId()).isNotNull();
        assertThat(foundShips.get(0).getName()).isEqualTo("Name");
        assertThat(foundShips.get(0).getDurability()).isEqualTo(400);
        assertThat(foundShips.get(0).getClazz()).isEqualTo(ShipClassEnum.MINER);
        assertThat(foundShips.get(0).getRank()).isEqualTo(ShipRankEnum.S);
        assertThat(foundShips.get(0).getUser().getId()).isEqualTo(this.player.getId());
        assertThat(foundShips.get(0).getOperator().getId()).isEqualTo(this.player.getId());
    }

}
