package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.Ship;

public interface CreateShip {

    Ship create(CreateShipCommand command);

    Ship createRandom();
}
