package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuest;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuestCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveUserPort;
import com.themonkeynauts.game.common.exception.BountyHuntingException;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import com.themonkeynauts.game.domain.ShipRankType;
import com.themonkeynauts.game.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartBountyHuntingQuestUseCaseTest {

    private StartBountyHuntingQuest startBountyHuntingQuest;

    @Mock
    private LoadShipPort loadShip;

    @Mock
    private SaveShipPort saveShip;

    @Mock
    private SaveUserPort saveUser;

    @Mock
    private SecurityUtils securityUtils;

    @BeforeEach
    public void setUp() {
        startBountyHuntingQuest = new StartBountyHuntingQuestUseCase(loadShip, saveShip, saveUser, securityUtils);
    }

    @Test
    public void shouldStartQuestAndDecreaseShipsDurability() {
        var shipId = UUID.randomUUID();
        var ship = new Ship(shipId, "Name", new ShipClass(ShipClassType.FIGHTER), new ShipRank(ShipRankType.A), new ShipAttributes(new ShipDurabilityAttribute(200)));
        when(loadShip.byId(shipId)).thenReturn(Optional.of(ship));

        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "nickname", "123456", new Account(UUID.randomUUID()));
        when(securityUtils.getAuthenticatedUser()).thenReturn(player);

        var savedShip = new Ship(shipId, "Name", new ShipClass(ShipClassType.FIGHTER), new ShipRank(ShipRankType.A), new ShipAttributes(new ShipDurabilityAttribute(0)));
        when(saveShip.save(ship)).thenReturn(savedShip);

        var command = new StartBountyHuntingQuestCommand(shipId);
        var result = startBountyHuntingQuest.with(command);

        assertThat(result.getAttributes().getCurrentDurability().getValue()).isEqualTo(0);

        verify(saveUser).startBountyHuntingQuest(eq(userId), any(LocalDateTime.class));
    }

    @Test
    public void shouldThrowExceptionIfPlayerHasPlayedBeforeResettingLastTimePlayed() {
        var shipId = UUID.randomUUID();
        var ship = new Ship(shipId, "Name", new ShipClass(ShipClassType.FIGHTER), new ShipRank(ShipRankType.A), new ShipAttributes(new ShipDurabilityAttribute(200)));
        when(loadShip.byId(shipId)).thenReturn(Optional.of(ship));

        var userId = UUID.randomUUID();
        var player = new User(userId, "email@email.com", "nickname", "123456", new Account(UUID.randomUUID()));
        player.setLastBountyHunting(LocalDateTime.now().minusHours(16));
        when(securityUtils.getAuthenticatedUser()).thenReturn(player);

        assertThatThrownBy(() -> {
            var command = new StartBountyHuntingQuestCommand(shipId);
            startBountyHuntingQuest.with(command);
        })
        .isInstanceOf(BountyHuntingException.class)
        .hasMessage("validation.bounty-hunting.waiting-time");
    }

}