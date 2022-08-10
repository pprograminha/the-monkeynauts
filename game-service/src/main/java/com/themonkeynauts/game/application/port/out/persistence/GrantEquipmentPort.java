package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.Equipment;

public interface GrantEquipmentPort {

    void grant(Equipment equipment);

}
