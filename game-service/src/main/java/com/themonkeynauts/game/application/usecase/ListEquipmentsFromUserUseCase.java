package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.ListEquipmentsFromUser;
import com.themonkeynauts.game.application.port.out.persistence.LoadEquipmentPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.domain.Equipment;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseAdapter
@RequiredArgsConstructor
public class ListEquipmentsFromUserUseCase implements ListEquipmentsFromUser {

    private final LoadEquipmentPort loadEquipment;

    @Override
    public List<Equipment> all() {
        return loadEquipment.all();
    }
}
