package com.themonkeynauts.game.adapter.out.random;

import com.github.javafaker.Faker;
import com.themonkeynauts.game.application.port.out.random.RandomGeneratorPort;
import com.themonkeynauts.game.common.exception.AttributeOutOfRangeException;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRankType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class RandomFakerGenerator implements RandomGeneratorPort {

    private final Faker faker;
    private final Random random;


    @Override
    public String firstName() {
        var name = faker.name().firstName();
        if ("Gaylord".equals(name) || "Gay".equals(name)) {
            return "Gary";
        }
        return name;
    }

    @Override
    public String lastName() {
        var name = faker.name().lastName();
        if ("Gaylord".equals(name)) {
            return "Gerhold";
        }
        return name;
    }

    @Override
    public MonkeynautClassType monkeynautClassType() {
        var percent = this.random.nextDouble() * 100;
        var previousChanceValue = 0;
        for (var type: MonkeynautClassType.values()) {
            var currentChanceValue = previousChanceValue + type.chance();
            if (isBetweenRange(percent, previousChanceValue, currentChanceValue)) {
                return type;
            }
            previousChanceValue = currentChanceValue;
        }
        throw new AttributeOutOfRangeException(MonkeynautClassType.class);
    }

    @Override
    public MonkeynautRankType monkeynautRankType() {
        var percent = random.nextDouble() * 100;
        var previousChanceValue = 0;
        for (var type: MonkeynautRankType.values()) {
            var currentChanceValue = previousChanceValue + type.chance();
            if (isBetweenRange(percent, previousChanceValue, currentChanceValue)) {
                return type;
            }
            previousChanceValue = currentChanceValue;
        }
        throw new AttributeOutOfRangeException(MonkeynautRankType.class);
    }

    @Override
    public MonkeynautAttributes monkeynautAttributes(MonkeynautRankType rankType) {
        var skill = random(MonkeynautSkillAttribute.LOW_LIMIT, MonkeynautSkillAttribute.HIGH_LIMIT);
        var speed = random(MonkeynautSpeedAttribute.LOW_LIMIT, MonkeynautSpeedAttribute.HIGH_LIMIT);
        var resistance = random(MonkeynautResistanceAttribute.LOW_LIMIT, MonkeynautResistanceAttribute.HIGH_LIMIT);
        var life = random(MonkeynautLifeAttribute.LOW_LIMIT, MonkeynautLifeAttribute.HIGH_LIMIT);
        var currentEnergy = rankType.baseEnergy();
        return new MonkeynautAttributes(new MonkeynautSkillAttribute(skill), new MonkeynautSpeedAttribute(speed), new MonkeynautResistanceAttribute(resistance), new MonkeynautLifeAttribute(life), new MonkeynautEnergyAttribute(currentEnergy));
    }

    @Override
    public String shipName() {
        var index = random((short) 0, (short) 6);
        var names = new String[] {
                faker.space().moon(),
                faker.space().galaxy(),
                faker.space().nebula(),
                faker.space().starCluster(),
                faker.space().star(),
                faker.space().constellation(),
                faker.space().meteorite()
        };
        var shipName = names[index];
        if ("Moon".equals(shipName)) {
            return names[index + 1];
        }
        if ("Sun".equals(shipName)) {
            return names[index + 1];
        }
        return shipName;
    }

    @Override
    public ShipClassType shipClassType() {
        var percent = this.random.nextDouble() * 100;
        var previousChanceValue = 0;
        for (var type: ShipClassType.values()) {
            var currentChanceValue = previousChanceValue + type.chance();
            if (isBetweenRange(percent, previousChanceValue, currentChanceValue)) {
                return type;
            }
            previousChanceValue = currentChanceValue;
        }
        throw new AttributeOutOfRangeException(ShipClassType.class);
    }

    @Override
    public ShipRankType shipRankType() {
        var percent = random.nextDouble() * 100;
        var previousChanceValue = 0;
        for (var type: ShipRankType.values()) {
            var currentChanceValue = previousChanceValue + type.chance();
            if (isBetweenRange(percent, previousChanceValue, currentChanceValue)) {
                return type;
            }
            previousChanceValue = currentChanceValue;
        }
        throw new AttributeOutOfRangeException(ShipRankType.class);
    }

    @Override
    public ShipAttributes shipAttributes(ShipRankType rankType) {
        var durability = rankType.tankCapacity();
        return new ShipAttributes(new ShipDurabilityAttribute(durability));
    }

    private int random(short low, short high) {
        var nextInt = random.nextInt(high - low);
        return nextInt + low;
    }

    private boolean isBetweenRange(double percent, int previousChanceValue, int currentChanceValue) {
        return percent >= previousChanceValue && percent < currentChanceValue;
    }
}
