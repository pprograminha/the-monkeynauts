package com.themonkeynauts.game.domain;

import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

@ToString
public class Ship {

    private UUID id;

    private User owner;
    private User operator;

    private final String name;
    private final ShipClass clazz;
    private final ShipRank rank;
    private final ShipCrew crew;
    private final ShipAttributes attributes;
    private final ShipBonus bountyHuntingMissionBonus;
    private final ShipBonus miningMissionBonus;
    private final ShipBonus explorationMissionTimeBonus;

    public Ship(UUID id, String name, ShipClass clazz, ShipRank rank, ShipAttributes attributes) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
        this.rank = rank;
        this.attributes = attributes;
        this.crew = new ShipCrew(id, rank.seats());
        this.bountyHuntingMissionBonus = new ShipBonus(clazz, rank.bountyHuntingMissionBonus());
        this.miningMissionBonus = new ShipBonus(clazz, rank.miningMissionBonus());
        this.explorationMissionTimeBonus = new ShipBonus(clazz, rank.explorationMissionBonus());
    }

    public Ship(String name, ShipClass clazz, ShipRank rank, ShipAttributes attributes) {
        this.name = name;
        this.clazz = clazz;
        this.rank = rank;
        this.attributes = attributes;
        this.crew = new ShipCrew(null, rank.seats());
        this.bountyHuntingMissionBonus = new ShipBonus(clazz, rank.bountyHuntingMissionBonus());
        this.miningMissionBonus = new ShipBonus(clazz, rank.miningMissionBonus());
        this.explorationMissionTimeBonus = new ShipBonus(clazz, rank.explorationMissionBonus());
    }

    public ShipBonus bonus() {
        return clazz.isFighter() ? this.bountyHuntingMissionBonus
                : clazz.isMiner() ? this.miningMissionBonus
                : clazz.isExplorer() ? this.explorationMissionTimeBonus
                : null;
    }

    public int currentDurability() {
        return this.getAttributes().getCurrentDurability().getValue();
    }

    public int maxDurability() {
        return this.rank.getType().tankCapacity();
    }

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public ShipClass getClazz() {
        return this.clazz;
    }

    public ShipRank getRank() {
        return this.rank;
    }

    public ShipAttributes getAttributes() {
        return this.attributes;
    }

    public ShipCrew getCrew() {
        return this.crew;
    }

    public User getOwner() {
        return this.owner;
    }

    public User getOperator() {
        return this.operator;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Ship ship = (Ship) o;

        return new EqualsBuilder().append(id, ship.id).append(owner, ship.owner).append(operator, ship.operator).append(name, ship.name).append(clazz, ship.clazz).append(rank, ship.rank).append(crew, ship.crew).append(attributes, ship.attributes).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(owner).append(operator).append(name).append(clazz).append(rank).append(crew).append(attributes).toHashCode();
    }

    public void makeTrip(int durabilityCost) {
        attributes.getCurrentDurability().decrease(durabilityCost);
    }
}
