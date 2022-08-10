package com.themonkeynauts.game.adapter.out.random;

import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.github.javafaker.Space;
import com.themonkeynauts.game.common.exception.AttributeOutOfRangeException;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipRankType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RandomFakerGeneratorTest {

    @Mock
    private Faker faker;

    @Mock
    private Name name;

    @Mock
    private Space space;

    @Mock
    private Random random;

    private RandomFakerGenerator randomFakerGenerator;

    @BeforeEach
    public void setUp() {
        randomFakerGenerator = new RandomFakerGenerator(faker, random);
    }

    @Test
    public void shouldGenerateRandomFirstName() {
        when(faker.name()).thenReturn(name);
        when(name.firstName()).thenReturn("Firstname");

        var result = randomFakerGenerator.firstName();

        assertThat(result).isEqualTo("Firstname");
    }

    @Test
    public void shouldGenerateRandomFirstNameButNotGay() {
        when(faker.name()).thenReturn(name);
        when(name.firstName()).thenReturn("Gay");

        var result = randomFakerGenerator.firstName();

        assertThat(result).isEqualTo("Gary");
    }

    @Test
    public void shouldGenerateRandomFirstNameButNotGaylord() {
        when(faker.name()).thenReturn(name);
        when(name.firstName()).thenReturn("Gaylord");

        var result = randomFakerGenerator.firstName();

        assertThat(result).isEqualTo("Gary");
    }

    @Test
    public void shouldGenerateRandomLastName() {
        when(faker.name()).thenReturn(name);
        when(name.lastName()).thenReturn("Lastname");

        var result = randomFakerGenerator.lastName();

        assertThat(result).isEqualTo("Lastname");
    }

    @Test
    public void shouldGenerateRandomLastNameButNotGaylord() {
        when(faker.name()).thenReturn(name);
        when(name.lastName()).thenReturn("Gaylord");

        var result = randomFakerGenerator.lastName();

        assertThat(result).isEqualTo("Gerhold");
    }

    @Test
    public void shouldGenerateEngineerMonkeynautClassType() {
        var limit = MonkeynautClassType.ENGINEER.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautClassType();

        assertThat(result).isEqualTo(MonkeynautClassType.ENGINEER);
    }

    @Test
    public void shouldGenerateScientistMonkeynautClassType() {
        var limit = MonkeynautClassType.ENGINEER.chance() + MonkeynautClassType.SCIENTIST.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautClassType();

        assertThat(result).isEqualTo(MonkeynautClassType.SCIENTIST);
    }

    @Test
    public void shouldGenerateSoldierMonkeynautClassType() {
        var limit = MonkeynautClassType.ENGINEER.chance() + MonkeynautClassType.SCIENTIST.chance() + MonkeynautClassType.SOLDIER.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautClassType();

        assertThat(result).isEqualTo(MonkeynautClassType.SOLDIER);
    }

    @Test
    public void shouldThrowExceptionForMonkeynautClassTypeLessThanZero() {
        var limit = -1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        assertThatThrownBy(() -> randomFakerGenerator.monkeynautClassType())
        .isInstanceOf(AttributeOutOfRangeException.class)
        .hasMessage("validation.attribute.out-of-range");
    }

    @Test
    public void shouldGeneratePrivateRankType() {
        var limit = MonkeynautRankType.PRIVATE.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautRankType();

        assertThat(result).isEqualTo(MonkeynautRankType.PRIVATE);
    }

    @Test
    public void shouldGenerateSergeantRankType() {
        var limit = MonkeynautRankType.PRIVATE.chance() + MonkeynautRankType.SERGEANT.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautRankType();

        assertThat(result).isEqualTo(MonkeynautRankType.SERGEANT);
    }

    @Test
    public void shouldGenerateCaptainRankType() {
        var limit = MonkeynautRankType.PRIVATE.chance() + MonkeynautRankType.SERGEANT.chance() + MonkeynautRankType.CAPTAIN.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautRankType();

        assertThat(result).isEqualTo(MonkeynautRankType.CAPTAIN);
    }

    @Test
    public void shouldGenerateMajorRankType() {
        var limit = MonkeynautRankType.PRIVATE.chance() + MonkeynautRankType.SERGEANT.chance() + MonkeynautRankType.CAPTAIN.chance() + MonkeynautRankType.MAJOR.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.monkeynautRankType();

        assertThat(result).isEqualTo(MonkeynautRankType.MAJOR);
    }

    @Test
    public void shouldThrowExceptionForMonkeynautRankTypeGreaterThanOneHundred() {
        var limit = 101;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        assertThatThrownBy(() -> randomFakerGenerator.monkeynautRankType())
                .isInstanceOf(AttributeOutOfRangeException.class)
                .hasMessage("validation.attribute.out-of-range");
    }

    @Test
    public void shouldGenerateMonkeynautRandomAttributes() {
        when(random.nextInt(30)).thenReturn(0);
        when(random.nextInt(100)).thenReturn(0);

        var attributes = randomFakerGenerator.monkeynautAttributes(MonkeynautRankType.MAJOR);

        assertThat(attributes.getSkill().getValue()).isEqualTo(20);
        assertThat(attributes.getSpeed().getValue()).isEqualTo(20);
        assertThat(attributes.getResistance().getValue()).isEqualTo(20);
        assertThat(attributes.getLife().getValue()).isEqualTo(250);
        assertThat(attributes.getCurrentEnergy().getValue()).isEqualTo(8);
    }

    @Test
    public void shouldGenerateRandomShipNameButNotSun() {
        when(random.nextInt(6)).thenReturn(4);
        when(faker.space()).thenReturn(space);
        when(space.star()).thenReturn("Sun");
        when(space.constellation()).thenReturn("Orion");

        var result = randomFakerGenerator.shipName();

        assertThat(result).isEqualTo("Orion");
    }

    @Test
    public void shouldGenerateRandomShipNameButNotMoon() {
        when(random.nextInt(6)).thenReturn(0);
        when(faker.space()).thenReturn(space);
        when(space.moon()).thenReturn("Moon");
        when(space.galaxy()).thenReturn("Milk Way");

        var result = randomFakerGenerator.shipName();

        assertThat(result).isEqualTo("Milk Way");
    }

    @Test
    public void shouldGenerateRandomShipName() {
        when(random.nextInt(6)).thenReturn(6);
        when(faker.space()).thenReturn(space);
        when(space.meteorite()).thenReturn("Zagami");

        var result = randomFakerGenerator.shipName();

        assertThat(result).isEqualTo("Zagami");
    }

    @Test
    public void shouldGenerateFighterShipClassType() {
        var limit = ShipClassType.FIGHTER.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.shipClassType();

        assertThat(result).isEqualTo(ShipClassType.FIGHTER);
    }

    @Test
    public void shouldGenerateMinerShipClassType() {
        var limit = ShipClassType.FIGHTER.chance() + ShipClassType.MINER.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.shipClassType();

        assertThat(result).isEqualTo(ShipClassType.MINER);
    }

    @Test
    public void shouldGenerateExplorerShipClassType() {
        var limit = ShipClassType.FIGHTER.chance() + ShipClassType.MINER.chance() + ShipClassType.EXPLORER.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.shipClassType();

        assertThat(result).isEqualTo(ShipClassType.EXPLORER);
    }

    @Test
    public void shouldThrowExceptionForShipClassTypeLessThanZero() {
        var limit = -1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        assertThatThrownBy(() -> randomFakerGenerator.shipClassType())
                .isInstanceOf(AttributeOutOfRangeException.class)
                .hasMessage("validation.attribute.out-of-range");
    }

    @Test
    public void shouldGenerateShipRankTypeB() {
        var limit = ShipRankType.B.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.shipRankType();

        assertThat(result).isEqualTo(ShipRankType.B);
    }

    @Test
    public void shouldGenerateShipRankTypeA() {
        var limit = ShipRankType.B.chance() + ShipRankType.A.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.shipRankType();

        assertThat(result).isEqualTo(ShipRankType.A);
    }

    @Test
    public void shouldGenerateShipRankTypeS() {
        var limit = ShipRankType.B.chance() + ShipRankType.A.chance() + ShipRankType.S.chance() - 1;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        var result = randomFakerGenerator.shipRankType();

        assertThat(result).isEqualTo(ShipRankType.S);
    }

    @Test
    public void shouldThrowExceptionForShipRankTypeGreaterThanOneHundred() {
        var limit = 101;
        var percent = (double) limit / 100;
        when(random.nextDouble()).thenReturn(percent);

        assertThatThrownBy(() -> randomFakerGenerator.shipRankType())
                .isInstanceOf(AttributeOutOfRangeException.class)
                .hasMessage("validation.attribute.out-of-range");
    }

    @Test
    public void shouldGenerateShipRandomAttributes() {
        var attributes = randomFakerGenerator.shipAttributes(ShipRankType.S);

        assertThat(attributes.getCurrentDurability().getValue()).isEqualTo(400);
    }

}