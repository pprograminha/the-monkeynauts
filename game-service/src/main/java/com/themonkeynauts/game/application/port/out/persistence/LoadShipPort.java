package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Ship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadShipPort {

    List<Ship> all();

    Optional<Ship> byId(UUID id);
}
