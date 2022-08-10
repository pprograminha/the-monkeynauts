package com.themonkeynauts.game.domain;

import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@ToString
public class MonkeynautAttributes {

    private final MonkeynautSkillAttribute skill;
    private final MonkeynautSpeedAttribute speed;
    private final MonkeynautResistanceAttribute resistance;
    private final MonkeynautLifeAttribute life;
    private final MonkeynautEnergyAttribute currentEnergy;

    public MonkeynautAttributes(MonkeynautSkillAttribute skill, MonkeynautSpeedAttribute speed, MonkeynautResistanceAttribute resistance, MonkeynautLifeAttribute life, MonkeynautEnergyAttribute currentEnergy) {
        this.skill = skill;
        this.speed = speed;
        this.resistance = resistance;
        this.life = life;
        this.currentEnergy = currentEnergy;
    }

    public MonkeynautSkillAttribute getSkill() {
        return this.skill;
    }

    public MonkeynautSpeedAttribute getSpeed() {
        return this.speed;
    }

    public MonkeynautResistanceAttribute getResistance() {
        return this.resistance;
    }

    public MonkeynautLifeAttribute getLife() {
        return this.life;
    }

    public MonkeynautEnergyAttribute getCurrentEnergy() {
        return this.currentEnergy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MonkeynautAttributes that = (MonkeynautAttributes) o;

        return new EqualsBuilder().append(skill, that.skill).append(speed, that.speed).append(resistance, that.resistance).append(life, that.life).append(currentEnergy, that.currentEnergy).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(skill).append(speed).append(resistance).append(life).append(currentEnergy).toHashCode();
    }
}
