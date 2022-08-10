package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Equipment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LoadEquipmentPort {

    Optional<Equipment> byId(UUID id);

    List<Equipment> all();
}
