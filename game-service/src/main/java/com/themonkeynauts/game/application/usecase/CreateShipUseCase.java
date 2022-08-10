package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateShip;
import com.themonkeynauts.game.application.port.in.CreateShipCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.application.port.out.random.RandomGeneratorPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.NumberOfShipsExceededException;
import com.themonkeynauts.game.common.exception.ShipRequiredAttributeException;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipRank;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class CreateShipUseCase implements CreateShip {

    private static final int MAX_SHIPS_PER_PLAYER = 1;

    private final RandomGeneratorPort randomGenerator;
    private final SaveShipPort saveShip;
    private final LoadShipPort loadShip;

    @Override
    public Ship create(CreateShipCommand command) {
        validate(command);
        var shipName = randomGenerator.shipName();
        var attributes = randomGenerator.shipAttributes(command.rank().getType());
        var ship = new Ship(shipName, command.clazz(), command.rank(), attributes);
        return saveShip.save(ship);
    }

    @Override
    public Ship createRandom() {
        var classType = randomGenerator.shipClassType();
        var rankType = randomGenerator.shipRankType();
        var command = new CreateShipCommand(new ShipClass(classType), new ShipRank(rankType));
        return create(command);
    }

    private void validate(CreateShipCommand command) {
        if (command.clazz() == null) {
            throw new ShipRequiredAttributeException("class");
        }
        if (command.rank() == null) {
            throw new ShipRequiredAttributeException("rank");
        }
        var ships = loadShip.all();
        if (ships.size() == MAX_SHIPS_PER_PLAYER) {
            throw new NumberOfShipsExceededException();
        }
    }
}
