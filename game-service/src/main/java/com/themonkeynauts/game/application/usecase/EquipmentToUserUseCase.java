package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.EquipmentToUser;
import com.themonkeynauts.game.application.port.in.EquipmentToUserCommand;
import com.themonkeynauts.game.application.port.out.persistence.GrantEquipmentPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadEquipmentPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.EquipmentNotFoundException;
import lombok.RequiredArgsConstructor;

@UseCaseAdapter
@RequiredArgsConstructor
public class EquipmentToUserUseCase implements EquipmentToUser {

    private final LoadEquipmentPort loadEquipment;
    private final GrantEquipmentPort grantEquipment;

    @Override
    public void grant(EquipmentToUserCommand command) {
        var equipment = loadEquipment.byId(command.equipmentId());
        if (equipment.isEmpty()) {
            throw new EquipmentNotFoundException(command.equipmentId());
        }
        grantEquipment.grant(equipment.get());
    }
}
