package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Monkeynaut;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadMonkeynautPort {

    List<Monkeynaut> all();

    Optional<Monkeynaut> byId(UUID id);
}
