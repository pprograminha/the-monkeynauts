package com.themonkeynauts.game.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MonkeynautTest {

    @Test
    public void shouldCalculatePrivateSoldierAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SOLDIER);
        var rank = new MonkeynautRank(MonkeynautRankType.PRIVATE);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var currentEnergy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(60);
        assertThat(speed).isEqualTo(40);
        assertThat(resistance).isEqualTo(30);
        assertThat(life).isEqualTo(250);
        assertThat(currentEnergy).isEqualTo(2);
        assertThat(maxEnergy).isEqualTo(2);
    }

    @Test
    public void shouldCalculateSergeantSoldierAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SOLDIER);
        var rank = new MonkeynautRank(MonkeynautRankType.SERGEANT);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(77);
        assertThat(speed).isEqualTo(50);
        assertThat(resistance).isEqualTo(37);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(4);
        assertThat(maxEnergy).isEqualTo(4);
    }

    @Test
    public void shouldCalculateCaptainSoldierAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SOLDIER);
        var rank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(95);
        assertThat(speed).isEqualTo(60);
        assertThat(resistance).isEqualTo(45);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(6);
        assertThat(maxEnergy).isEqualTo(6);
    }

    @Test
    public void shouldCalculateMajorSoldierAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SOLDIER);
        var rank = new MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(8));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(112);
        assertThat(speed).isEqualTo(70);
        assertThat(resistance).isEqualTo(52);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(8);
        assertThat(maxEnergy).isEqualTo(8);
    }

    @Test
    public void shouldCalculatePrivateScientistAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new MonkeynautRank(MonkeynautRankType.PRIVATE);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(50);
        assertThat(speed).isEqualTo(48);
        assertThat(resistance).isEqualTo(30);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(2);
        assertThat(maxEnergy).isEqualTo(2);
    }

    @Test
    public void shouldCalculateSergeantScientistAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new MonkeynautRank(MonkeynautRankType.SERGEANT);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(62);
        assertThat(speed).isEqualTo(62);
        assertThat(resistance).isEqualTo(37);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(4);
        assertThat(maxEnergy).isEqualTo(4);
    }

    @Test
    public void shouldCalculateCaptainScientistAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(75);
        assertThat(speed).isEqualTo(76);
        assertThat(resistance).isEqualTo(45);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(6);
        assertThat(maxEnergy).isEqualTo(6);
    }

    @Test
    public void shouldCalculateMajorScientistAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(8));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(87);
        assertThat(speed).isEqualTo(90);
        assertThat(resistance).isEqualTo(52);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(8);
        assertThat(maxEnergy).isEqualTo(8);
    }

    @Test
    public void shouldCalculatePrivateEngineerAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.PRIVATE);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(50);
        assertThat(speed).isEqualTo(40);
        assertThat(resistance).isEqualTo(36);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(2);
        assertThat(maxEnergy).isEqualTo(2);
    }

    @Test
    public void shouldCalculateSergeantEngineerAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.SERGEANT);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(4));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(62);
        assertThat(speed).isEqualTo(50);
        assertThat(resistance).isEqualTo(46);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(4);
        assertThat(maxEnergy).isEqualTo(4);
    }

    @Test
    public void shouldCalculateCaptainEngineerAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(75);
        assertThat(speed).isEqualTo(60);
        assertThat(resistance).isEqualTo(57);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(6);
        assertThat(maxEnergy).isEqualTo(6);
    }

    @Test
    public void shouldCalculateMajorEngineerAttributes() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(8));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var skill = monkeynaut.skill();
        var speed = monkeynaut.speed();
        var resistance = monkeynaut.resistance();
        var life = monkeynaut.life();
        var energy = monkeynaut.currentEnergy();
        var maxEnergy = monkeynaut.maxEnergy();

        assertThat(skill).isEqualTo(87);
        assertThat(speed).isEqualTo(70);
        assertThat(resistance).isEqualTo(67);
        assertThat(life).isEqualTo(250);
        assertThat(energy).isEqualTo(8);
        assertThat(maxEnergy).isEqualTo(8);
    }

    @Test
    public void shouldEquipEquipment() {
        var id = UUID.randomUUID();
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(350), new MonkeynautEnergyAttribute(8));
        var monkeynaut = new Monkeynaut(id, "FirstName", "LastName", clazz, rank, attributes);

        var weaponId = UUID.randomUUID();
        var equipment = new Equipment(weaponId, "Weapon", EquipmentType.WEAPON);
        monkeynaut.equip(equipment);

        assertThat(monkeynaut.isEquippedWith(equipment)).isTrue();
    }

}