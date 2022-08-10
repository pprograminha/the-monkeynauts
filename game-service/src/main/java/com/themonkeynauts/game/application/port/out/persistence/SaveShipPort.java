package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Ship;

public interface SaveShipPort {

    Ship save(Ship ship);
}
