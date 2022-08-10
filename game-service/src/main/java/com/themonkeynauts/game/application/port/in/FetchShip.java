package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.Ship;

import java.util.UUID;

public interface FetchShip {

    Ship byId(UUID id);
}
