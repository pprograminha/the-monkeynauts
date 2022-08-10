package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.MonkeynautAlreadyInCrewException;
import com.themonkeynauts.game.common.exception.ShipFullException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

public class ShipCrew {

    private final UUID shipId;
    private final Integer seats;

    private final Set<Monkeynaut> monkeynauts;

    public ShipCrew(UUID shipId, int seats) {
        this.shipId = shipId;
        this.seats = seats;
        this.monkeynauts = new LinkedHashSet<>();
    }

    public void addMonkeynaut(Monkeynaut monkeynaut) {
        if (this.monkeynauts.size() == seats) {
            throw new ShipFullException();
        }
        if (monkeynaut.getShipId() != null && !monkeynaut.getShipId().equals(shipId) && monkeynaut.isCrewMember()) {
            throw new MonkeynautAlreadyInCrewException(monkeynaut.getShipId());
        }
        this.monkeynauts.add(monkeynaut);
        monkeynaut.setShipId(shipId);
    }

    public void removeMonkeynaut(Monkeynaut monkeynaut) {
        this.monkeynauts.removeIf(m -> m.getId().equals(monkeynaut.getId()));
        monkeynaut.setShipId(null);
    }

    public Set<Monkeynaut> monkeynauts() {
        return this.monkeynauts;
    }

    public Integer seats() {
        return this.seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ShipCrew shipCrew = (ShipCrew) o;

        return new EqualsBuilder().append(shipId, shipCrew.shipId).append(seats, shipCrew.seats).append(monkeynauts, shipCrew.monkeynauts).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(shipId).append(seats).append(monkeynauts).toHashCode();
    }
}
