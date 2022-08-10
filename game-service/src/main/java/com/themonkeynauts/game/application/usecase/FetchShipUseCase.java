package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.FetchShip;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.NotFoundException;
import com.themonkeynauts.game.domain.Ship;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCaseAdapter
@RequiredArgsConstructor
public class FetchShipUseCase implements FetchShip {

    private final LoadShipPort loadShip;

    @Override
    public Ship byId(UUID id) {
        var ship = loadShip.byId(id);
        if (ship.isEmpty()) {
            throw new NotFoundException("entity.ship.name");
        }
        return ship.get();
    }
}
