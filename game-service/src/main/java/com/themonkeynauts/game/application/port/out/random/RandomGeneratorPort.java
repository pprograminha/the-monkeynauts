package com.themonkeynauts.game.application.port.out.random;

import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipRankType;

public interface RandomGeneratorPort {

    String firstName();

    String lastName();

    MonkeynautClassType monkeynautClassType();

    MonkeynautRankType monkeynautRankType();

    MonkeynautAttributes monkeynautAttributes(MonkeynautRankType rankType);

    String shipName();

    ShipClassType shipClassType();

    ShipRankType shipRankType();

    ShipAttributes shipAttributes(ShipRankType rankType);
}
