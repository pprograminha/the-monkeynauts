package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.ListShip;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import com.themonkeynauts.game.domain.ShipRankType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListShipUseCaseTest {

    private ListShip listShip;

    @Mock
    private LoadShipPort loadShip;

    @BeforeEach
    public void setUp() {
        listShip = new ListShipUseCase(loadShip);
    }

    @Test
    public void shouldListAllShips() {
        var id = UUID.randomUUID();
        var attributes = new ShipAttributes(new ShipDurabilityAttribute(6));
        var ship = new Ship(id, "Name", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.S), attributes);
        var ships = Arrays.asList(ship);
        when(loadShip.all()).thenReturn(ships);

        var result = listShip.all();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(id);
    }

}