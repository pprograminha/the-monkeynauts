package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrew;
import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrewCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.common.exception.NotFoundException;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRank;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddMonkeynautToCrewUseCaseTest {

    @Mock
    private LoadShipPort loadShip;

    @Mock
    private SaveShipPort saveShip;

    @Mock
    private LoadMonkeynautPort loadMonkeynaut;

    @Mock
    private SaveMonkeynautPort saveMonkeynaut;

    private AddMonkeynautToCrew addMonkeynautToCrew;

    @BeforeEach
    public void setUp() {
        addMonkeynautToCrew = new AddMonkeynautToCrewUseCase(loadShip, saveShip, loadMonkeynaut, saveMonkeynaut);
    }

    @Test
    public void shouldAddMonkeynautToShipCrew() {
        var shipId = UUID.randomUUID();
        var ship = new Ship(shipId, "Europa", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.S), new ShipAttributes(new ShipDurabilityAttribute(400)));
        when(loadShip.byId(shipId)).thenReturn(Optional.of(ship));

        var monkeynautId = UUID.randomUUID();
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut = new Monkeynaut(monkeynautId, "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes);
        when(loadMonkeynaut.byId(monkeynautId)).thenReturn(Optional.of(monkeynaut));

        addMonkeynautToCrew.ofShip(new AddMonkeynautToCrewCommand(shipId, monkeynautId));

        assertThat(ship.getCrew().monkeynauts()).hasSize(1);
        assertThat(ship.getCrew().monkeynauts()).containsExactly(monkeynaut);

        verify(saveShip).save(ship);
        verify(saveMonkeynaut).save(monkeynaut);
    }

    @Test
    public void shouldThrowExceptionIfShipIsNotFound() {
        var shipId = UUID.randomUUID();
        when(loadShip.byId(shipId)).thenReturn(Optional.empty());

        var monkeynautId = UUID.randomUUID();

        assertThatThrownBy(() -> {
            addMonkeynautToCrew.ofShip(new AddMonkeynautToCrewCommand(shipId, monkeynautId));
        })
        .isInstanceOf(NotFoundException.class)
        .hasMessage("validation.entity.not-found");
    }

    @Test
    public void shouldThrowExceptionIfMonkeynautIsNotFound() {
        var shipId = UUID.randomUUID();
        var ship = new Ship(shipId, "Europa", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.S), new ShipAttributes(new ShipDurabilityAttribute(400)));
        when(loadShip.byId(shipId)).thenReturn(Optional.of(ship));

        var monkeynautId = UUID.randomUUID();
        when(loadMonkeynaut.byId(monkeynautId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            addMonkeynautToCrew.ofShip(new AddMonkeynautToCrewCommand(shipId, monkeynautId));
        })
        .isInstanceOf(NotFoundException.class)
        .hasMessage("validation.entity.not-found");
    }

}