package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.ListShip;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.domain.Ship;
import lombok.RequiredArgsConstructor;

import java.util.List;

@UseCaseAdapter
@RequiredArgsConstructor
public class ListShipUseCase implements ListShip {

    private final LoadShipPort loadShip;

    @Override
    public List<Ship> all() {
        return loadShip.all();
    }
}
