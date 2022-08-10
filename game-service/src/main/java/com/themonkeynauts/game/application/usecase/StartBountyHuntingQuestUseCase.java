package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuest;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuestCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveUserPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.BountyHuntingException;
import com.themonkeynauts.game.common.exception.NotFoundException;
import com.themonkeynauts.game.domain.Ship;
import lombok.RequiredArgsConstructor;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@UseCaseAdapter
@RequiredArgsConstructor
public class StartBountyHuntingQuestUseCase implements StartBountyHuntingQuest {

    private static final int QUEST_FUEL_COST = 100;

    private final LoadShipPort loadShip;
    private final SaveShipPort saveShip;
    private final SaveUserPort saveUser;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public Ship with(StartBountyHuntingQuestCommand command) {
        var optionalShip = loadShip.byId(command.shipId());
        if (optionalShip.isEmpty()) {
            throw new NotFoundException("entity.ship.name");
        }
        var ship = optionalShip.get();
        ship.makeTrip(QUEST_FUEL_COST);

        var player = securityUtils.getAuthenticatedUser();
        if (player.getLastBountyHunting() != null) {
            throw new BountyHuntingException("validation.bounty-hunting.waiting-time");
        }
        saveUser.startBountyHuntingQuest(player.getId(), LocalDateTime.now());

        return saveShip.save(ship);
    }
}
