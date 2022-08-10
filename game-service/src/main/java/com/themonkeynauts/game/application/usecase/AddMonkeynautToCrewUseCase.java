package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrew;
import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrewCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class AddMonkeynautToCrewUseCase implements AddMonkeynautToCrew {

    private final LoadShipPort loadShip;
    private final SaveShipPort saveShip;
    private final LoadMonkeynautPort loadMonkeynaut;
    private final SaveMonkeynautPort saveMonkeynaut;

    @Override
    public void ofShip(AddMonkeynautToCrewCommand command) {
        var optionalShip = loadShip.byId(command.shipId());
        if (optionalShip.isEmpty()) {
            throw new NotFoundException("entity.ship.name");
        }
        var optionalMonkeynaut = loadMonkeynaut.byId(command.monkeynautId());
        if (optionalMonkeynaut.isEmpty()) {
            throw new NotFoundException("entity.monkeynaut.name");
        }
        var ship = optionalShip.get();
        var monkeynaut = optionalMonkeynaut.get();
        ship.getCrew().addMonkeynaut(monkeynaut);
        saveShip.save(ship);
        saveMonkeynaut.save(monkeynaut);
    }
}
