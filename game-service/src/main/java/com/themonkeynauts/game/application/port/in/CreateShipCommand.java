package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipRank;

public record CreateShipCommand(ShipClass clazz, ShipRank rank) {}
