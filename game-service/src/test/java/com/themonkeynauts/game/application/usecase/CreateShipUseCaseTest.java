package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateShip;
import com.themonkeynauts.game.application.port.in.CreateShipCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.application.port.out.random.RandomGeneratorPort;
import com.themonkeynauts.game.common.exception.NumberOfShipsExceededException;
import com.themonkeynauts.game.common.exception.ShipRequiredAttributeException;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateShipUseCaseTest {

    @Mock
    private RandomGeneratorPort randomGenerator;

    @Mock
    private SaveShipPort saveShip;

    @Mock
    private LoadShipPort loadShip;

    private CreateShip createShip;

    @BeforeEach
    public void setUp() {
        createShip = new CreateShipUseCase(randomGenerator, saveShip, loadShip);
    }

    @Test
    public void shouldCreateShip() {
        when(randomGenerator.shipName()).thenReturn("Saturn V");
        var attributes = new ShipAttributes(new ShipDurabilityAttribute(400));
        when(randomGenerator.shipAttributes(ShipRankType.S)).thenReturn(attributes);

        var clazz = new ShipClass(ShipClassType.FIGHTER);
        var rank = new ShipRank(ShipRankType.S);
        var ship = new Ship("Saturn V", clazz, rank, attributes);
        var id = UUID.randomUUID();
        var savedShip = new Ship(id, "Saturn V", clazz, rank, attributes);
        when(saveShip.save(ship)).thenReturn(savedShip);

        var command = new CreateShipCommand(clazz, rank);
        var result = createShip.create(command);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getClazz()).isEqualTo(new ShipClass(ShipClassType.FIGHTER));
        assertThat(result.getRank()).isEqualTo(new ShipRank(ShipRankType.S));
        assertThat(result.getAttributes().getCurrentDurability().getValue()).isEqualTo(400);
    }

    @Test
    public void shouldCreateRandomShip() {
        when(randomGenerator.shipName()).thenReturn("Saturn V");
        when(randomGenerator.shipClassType()).thenReturn(ShipClassType.EXPLORER);
        when(randomGenerator.shipRankType()).thenReturn(ShipRankType.A);
        var attributes = new ShipAttributes(new ShipDurabilityAttribute(300));
        when(randomGenerator.shipAttributes(ShipRankType.A)).thenReturn(attributes);

        var clazz = new ShipClass(ShipClassType.EXPLORER);
        var rank = new ShipRank(ShipRankType.A);
        var ship = new Ship("Saturn V", clazz, rank, attributes);
        var id = UUID.randomUUID();
        var savedShip = new Ship(id, "Saturn V", clazz, rank, attributes);
        when(saveShip.save(ship)).thenReturn(savedShip);

        var result = createShip.createRandom();

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getClazz()).isEqualTo(new ShipClass(ShipClassType.EXPLORER));
        assertThat(result.getRank()).isEqualTo(new ShipRank(ShipRankType.A));
        assertThat(result.getAttributes().getCurrentDurability().getValue()).isEqualTo(300);
    }

    @Test
    public void shouldThrowExceptionIfClassIsAbsent() {
        var command = new CreateShipCommand(null, new ShipRank(ShipRankType.B));

        assertThatThrownBy(() -> createShip.create(command))
                .isInstanceOf(ShipRequiredAttributeException.class)
                .hasMessage("validation.required.ship");
    }

    @Test
    public void shouldThrowExceptionIfRankIsAbsent() {
        var command = new CreateShipCommand(new ShipClass(ShipClassType.MINER), null);

        assertThatThrownBy(() -> createShip.create(command))
                .isInstanceOf(ShipRequiredAttributeException.class)
                .hasMessage("validation.required.ship");
    }

    @Test
    public void shouldThrowExceptionIfNumberOfShipsIsExceeded() {
        var clazz = new ShipClass(ShipClassType.EXPLORER);
        var rank = new ShipRank(ShipRankType.S);
        var durability = new ShipDurabilityAttribute(400);
        var attributes = new ShipAttributes(durability);
        var ship = new Ship(UUID.randomUUID(), "Europa", clazz, rank, attributes);
        when(loadShip.all()).thenReturn(List.of(ship));

        var command = new CreateShipCommand(clazz, rank);

        assertThatThrownBy(() -> createShip.create(command))
                .isInstanceOf(NumberOfShipsExceededException.class)
                .hasMessage("validation.ship.number-exceeded");
    }

}