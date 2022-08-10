package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateResourceForUser;
import com.themonkeynauts.game.application.port.in.CreateResourceForUserCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadUserPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.application.port.out.random.RandomGeneratorPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.NotFoundException;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautRank;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class CreateResourceForUserUseCase implements CreateResourceForUser {

    private final LoadUserPort loadUser;
    private final RandomGeneratorPort randomGenerator;
    private final SaveShipPort saveShip;
    private final SaveMonkeynautPort saveMonkeynaut;

    @Override
    public Ship ship(CreateResourceForUserCommand command) {
        var user = loadUser.byEmail(command.email()).orElseThrow(() -> new NotFoundException("entity.user.name"));
        var name = randomGenerator.shipName();
        var clazz = randomGenerator.shipClassType();
        var rank = randomGenerator.shipRankType();
        var attributes = new ShipAttributes(new ShipDurabilityAttribute(rank.tankCapacity()));
        var ship = new Ship(name, new ShipClass(clazz), new ShipRank(rank), attributes);
        ship.setOwner(user);
        ship.setOperator(user);
        return saveShip.save(ship);
    }

    @Override
    public Monkeynaut monkeynaut(CreateResourceForUserCommand command) {
        var user = loadUser.byEmail(command.email()).orElseThrow(() -> new NotFoundException("entity.user.name"));
        var firstName = randomGenerator.firstName();
        var lastName = randomGenerator.lastName();
        var clazz = randomGenerator.monkeynautClassType();
        var rank = randomGenerator.monkeynautRankType();
        var attributes = randomGenerator.monkeynautAttributes(rank);
        var monkeynaut = new Monkeynaut(firstName, lastName, new MonkeynautClass(clazz), new MonkeynautRank(rank), attributes);
        monkeynaut.setPlayer(user);
        monkeynaut.setOperator(user);
        return saveMonkeynaut.save(monkeynaut);
    }
}
