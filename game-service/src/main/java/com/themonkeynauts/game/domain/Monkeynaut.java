package com.themonkeynauts.game.domain;

import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.UUID;

@ToString
public class Monkeynaut {

    private UUID id;
    private Long number;

    private User player;
    private User operator;
    private UUID shipId;

    private final String firstName;
    private final String lastName;
    private final MonkeynautClass clazz;
    private final MonkeynautRank rank;
    private final MonkeynautAttributes attributes;
    private final MonkeynautEquipmentBox equipmentBox;
    private final Integer breedCount;
    private final MonkeynautBonus bountyHuntingMissionBonus;
    private final MonkeynautBonus miningMissionBonus;
    private final MonkeynautBonus explorationMissionBonus;

    public Monkeynaut(UUID id, String firstName, String lastName, MonkeynautClass clazz, MonkeynautRank rank, MonkeynautAttributes attributes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.clazz = clazz;
        this.rank = rank;
        this.attributes = attributes;
        this.equipmentBox = new MonkeynautEquipmentBox();
        this.breedCount = 0;
        this.bountyHuntingMissionBonus = new MonkeynautBonus(clazz, rank.bountyHuntingMissionBonus());
        this.miningMissionBonus = new MonkeynautBonus(clazz, rank.miningMissionBonus());
        this.explorationMissionBonus = new MonkeynautBonus(clazz, rank.explorationMissionBonus());
    }

    public Monkeynaut(String firstName, String lastName, MonkeynautClass clazz, MonkeynautRank rank, MonkeynautAttributes attributes) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.clazz = clazz;
        this.rank = rank;
        this.attributes = attributes;
        this.equipmentBox = new MonkeynautEquipmentBox();
        this.breedCount = 0;
        this.bountyHuntingMissionBonus = new MonkeynautBonus(clazz, rank.bountyHuntingMissionBonus());
        this.miningMissionBonus = new MonkeynautBonus(clazz, rank.miningMissionBonus());
        this.explorationMissionBonus = new MonkeynautBonus(clazz, rank.explorationMissionBonus());
    }

    public MonkeynautBonus bonus() {
        return clazz.isSoldier() ? this.bountyHuntingMissionBonus
                : clazz.isEngineer() ? this.miningMissionBonus
                : clazz.isScientist() ? this.explorationMissionBonus
                : null;
    }

    public int skill() {
        var baseSkill = this.attributes.getSkill().getValue();
        var rankBonus = (int) (baseSkill * this.rank.getType().rankBonus());
        var classBonus = (int) (getClazz().isSoldier() ? baseSkill * this.rank.getType().classBonus() : 0);

        return baseSkill + rankBonus + classBonus;
    }

    public int speed() {
        var baseSpeed = this.attributes.getSpeed().getValue();
        var rankBonus = (int) (baseSpeed * this.rank.getType().rankBonus());
        var classBonus = (int) (getClazz().isScientist() ? baseSpeed * this.rank.getType().classBonus() : 0);

        return baseSpeed + rankBonus + classBonus;
    }

    public int resistance() {
        var baseResistance = this.attributes.getResistance().getValue();
        var rankBonus = (int) (baseResistance * this.rank.getType().rankBonus());
        var classBonus = (int) (getClazz().isEngineer() ? baseResistance * this.rank.getType().classBonus() : 0);

        return baseResistance + rankBonus + classBonus;
    }

    public int life() {
        return this.attributes.getLife().getValue();
    }

    public int currentEnergy() {
        return this.attributes.getCurrentEnergy().getValue();
    }

    public int maxEnergy() {
        return this.rank.getType().baseEnergy();
    }

    public void equip(Equipment equipment) {
        equipmentBox.add(equipment);
    }

    public boolean isEquippedWith(Equipment equipment) {
        return this.equipmentBox.has(equipment);
    }

    public boolean isCrewMember() {
        return this.shipId != null;
    }

    public UUID getId() {
        return this.id;
    }

    public Long getNumber() {
        return this.number;
    }

    public User getPlayer() {
        return this.player;
    }

    public User getOperator() {
        return this.operator;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public MonkeynautClass getClazz() {
        return this.clazz;
    }

    public MonkeynautRank getRank() {
        return this.rank;
    }

    public MonkeynautAttributes getAttributes() {
        return this.attributes;
    }

    public Integer getBreedCount() {
        return this.breedCount;
    }

    public UUID getShipId() {
        return this.shipId;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public void setPlayer(User user) {
        this.player = user;
    }

    public void setOperator(User user) {
        this.operator = user;
    }

    public void setShipId(UUID shipId) {
        this.shipId = shipId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Monkeynaut that = (Monkeynaut) o;

        return new EqualsBuilder().append(id, that.id).append(number, that.number).append(player, that.player).append(operator, that.operator).append(firstName, that.firstName).append(lastName, that.lastName).append(clazz, that.clazz).append(rank, that.rank).append(attributes, that.attributes).append(equipmentBox, that.equipmentBox).append(breedCount, that.breedCount).append(shipId, that.shipId).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(number).append(player).append(operator).append(firstName).append(lastName).append(clazz).append(rank).append(attributes).append(equipmentBox).append(breedCount).append(shipId).toHashCode();
    }
}
