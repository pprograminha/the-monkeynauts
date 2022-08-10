package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.FetchShip;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.common.exception.NotFoundException;
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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FetchShipUseCaseTest {

    private FetchShip fetchShip;

    @Mock
    private LoadShipPort loadShip;

    @BeforeEach
    public void setUp() {
        fetchShip = new FetchShipUseCase(loadShip);
    }

    @Test
    public void shouldFetchShipById() {
        var shipId = UUID.randomUUID();
        var ship = new Ship(shipId, "Name", new ShipClass(ShipClassType.FIGHTER), new ShipRank(ShipRankType.A), new ShipAttributes(new ShipDurabilityAttribute(200)));
        when(loadShip.byId(shipId)).thenReturn(Optional.of(ship));

        var result = fetchShip.byId(shipId);

        assertThat(result).isEqualTo(ship);
    }

    @Test
    public void shouldThrowExceptionIfShipDoesNotExist() {
        var shipId = UUID.randomUUID();

        assertThatThrownBy(() -> {
            fetchShip.byId(shipId);
        })
        .isInstanceOf(NotFoundException.class)
        .hasMessage("validation.entity.not-found");
    }

}