package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.MonkeynautAlreadyInCrewException;
import com.themonkeynauts.game.common.exception.ShipFullException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShipCrewTest {

    @Test
    public void shouldAddMonkeynautToCrew() {
        var shipId = UUID.randomUUID();
        var crew = new ShipCrew(shipId,2);

        var monkeynautId = UUID.randomUUID();
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut = new Monkeynaut(monkeynautId, "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes);
        crew.addMonkeynaut(monkeynaut);

        assertThat(crew.monkeynauts()).hasSize(1);
        assertThat(crew.monkeynauts()).containsExactly(monkeynaut);
    }

    @Test
    public void shouldRemoveMonkeynautFromCrew() {
        var shipId = UUID.randomUUID();
        var crew = new ShipCrew(shipId,2);

        var attributes1 = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut1 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes1);
        crew.addMonkeynaut(monkeynaut1);

        var attributes2 = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut2 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes2);
        crew.addMonkeynaut(monkeynaut2);

        crew.removeMonkeynaut(monkeynaut1);

        assertThat(crew.monkeynauts()).hasSize(1);
        assertThat(crew.monkeynauts()).containsExactly(monkeynaut2);
        assertThat(monkeynaut1.getShipId()).isNull();
    }

    @Test
    public void shouldThrowExceptionIfShipIsFull() {
        var shipId = UUID.randomUUID();
        var crew = new ShipCrew(shipId,1);

        var attributes1 = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut1 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes1);
        crew.addMonkeynaut(monkeynaut1);

        assertThatThrownBy(() -> {
            var attributes2 = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
            var monkeynaut2 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes2);
            crew.addMonkeynaut(monkeynaut2);
        })
        .isInstanceOf(ShipFullException.class)
        .hasMessage("validation.ship.full");
    }

    @Test
    public void shouldThrowExceptionIfMonkeynautIsAlreadyInCrew() {
        var shipId = UUID.randomUUID();
        var crew = new ShipCrew(shipId,1);

        var attributes1 = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut1 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes1);
        monkeynaut1.setShipId(UUID.randomUUID());

        assertThatThrownBy(() -> {
            crew.addMonkeynaut(monkeynaut1);
        })
        .isInstanceOf(MonkeynautAlreadyInCrewException.class)
        .hasMessage("validation.ship.monkeynaut-already-in");

        assertThat(crew.monkeynauts()).hasSize(0);
    }

}