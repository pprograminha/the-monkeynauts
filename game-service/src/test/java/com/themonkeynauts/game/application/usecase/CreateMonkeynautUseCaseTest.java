package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.CreateMonkeynaut;
import com.themonkeynauts.game.application.port.in.CreateMonkeynautCommand;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveMonkeynautPort;
import com.themonkeynauts.game.application.port.out.random.RandomGeneratorPort;
import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import com.themonkeynauts.game.common.exception.NumberOfMonkeynautsExceededException;
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
class CreateMonkeynautUseCaseTest {

    @Mock
    private RandomGeneratorPort randomGenerator;

    @Mock
    private SaveMonkeynautPort saveMonkeynaut;

    @Mock
    private LoadMonkeynautPort loadMonkeynaut;

    private CreateMonkeynaut createMonkeynaut;

    @BeforeEach
    public void setUp() {
        createMonkeynaut = new CreateMonkeynautUseCase(randomGenerator, saveMonkeynaut, loadMonkeynaut);
    }

    @Test
    public void shouldCreateMonkeynaut() {
        when(randomGenerator.firstName()).thenReturn("Firstname");
        when(randomGenerator.lastName()).thenReturn("Lastname");
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        when(randomGenerator.monkeynautAttributes(MonkeynautRankType.CAPTAIN)).thenReturn(attributes);

        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);
        var monkeynaut = new Monkeynaut("Firstname", "Lastname", clazz, rank, attributes);
        var id = UUID.randomUUID();
        var savedMonkeynaut = new Monkeynaut(id, "Firstname", "Lastname", clazz, rank, attributes);
        when(saveMonkeynaut.save(monkeynaut)).thenReturn(savedMonkeynaut);

        var command = new CreateMonkeynautCommand(new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN));
        var result = createMonkeynaut.create(command);

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo("Firstname");
        assertThat(result.getLastName()).isEqualTo("Lastname");
        assertThat(result.getClazz().isEngineer()).isTrue();
        assertThat(result.getAttributes().getSkill().getValue()).isEqualTo(50);
        assertThat(result.getAttributes().getSpeed().getValue()).isEqualTo(40);
        assertThat(result.getAttributes().getResistance().getValue()).isEqualTo(30);
        assertThat(result.getAttributes().getLife().getValue()).isEqualTo(250);
        assertThat(result.getAttributes().getCurrentEnergy().getValue()).isEqualTo(6);
        assertThat(result.maxEnergy()).isEqualTo(6);
    }

    @Test
    public void shouldCreateRandomMonkeynaut() {
        when(randomGenerator.firstName()).thenReturn("Firstname");
        when(randomGenerator.lastName()).thenReturn("Lastname");
        when(randomGenerator.monkeynautClassType()).thenReturn(MonkeynautClassType.SCIENTIST);
        when(randomGenerator.monkeynautRankType()).thenReturn(MonkeynautRankType.PRIVATE);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(2));
        when(randomGenerator.monkeynautAttributes(MonkeynautRankType.PRIVATE)).thenReturn(attributes);

        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);
        var rank = new MonkeynautRank(MonkeynautRankType.PRIVATE);
        var monkeynaut = new Monkeynaut("Firstname", "Lastname", clazz, rank, attributes);
        var id = UUID.randomUUID();
        var savedMonkeynaut = new Monkeynaut(id, "Firstname", "Lastname", clazz, rank, attributes);
        when(saveMonkeynaut.save(monkeynaut)).thenReturn(savedMonkeynaut);

        var result = createMonkeynaut.createRandom();

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getFirstName()).isEqualTo("Firstname");
        assertThat(result.getLastName()).isEqualTo("Lastname");
        assertThat(result.getClazz().isScientist()).isTrue();
        assertThat(result.getAttributes().getSkill().getValue()).isEqualTo(50);
        assertThat(result.getAttributes().getSpeed().getValue()).isEqualTo(40);
        assertThat(result.getAttributes().getResistance().getValue()).isEqualTo(30);
        assertThat(result.getAttributes().getLife().getValue()).isEqualTo(250);
        assertThat(result.getAttributes().getCurrentEnergy().getValue()).isEqualTo(2);
        assertThat(result.maxEnergy()).isEqualTo(2);
    }

    @Test
    public void shouldThrowExceptionIfClassIsAbsent() {
        var command = new CreateMonkeynautCommand(null, new MonkeynautRank(MonkeynautRankType.PRIVATE));

        assertThatThrownBy(() -> createMonkeynaut.create(command))
        .isInstanceOf(MonkeynautRequiredAttributeException.class)
        .hasMessage("validation.required.monkeynaut");
    }

    @Test
    public void shouldThrowExceptionIfRankIsAbsent() {
        var command = new CreateMonkeynautCommand(new MonkeynautClass(MonkeynautClassType.ENGINEER), null);

        assertThatThrownBy(() -> createMonkeynaut.create(command))
                .isInstanceOf(MonkeynautRequiredAttributeException.class)
                .hasMessage("validation.required.monkeynaut");
    }

    @Test
    public void shouldThrowExceptionIfNumberOfMonkeynautsIsExceeded() {
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);
        var rank = new MonkeynautRank(MonkeynautRankType.MAJOR);
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(50), new MonkeynautResistanceAttribute(50), new MonkeynautLifeAttribute(350), new MonkeynautEnergyAttribute(4));
        var monkeynaut1 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", clazz, rank, attributes);
        var monkeynaut2 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", clazz, rank, attributes);
        var monkeynaut3 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", clazz, rank, attributes);
        var monkeynaut4 = new Monkeynaut(UUID.randomUUID(), "Firstname", "Lastname", clazz, rank, attributes);
        when(loadMonkeynaut.all()).thenReturn(List.of(monkeynaut1, monkeynaut2, monkeynaut3, monkeynaut4));

        var command = new CreateMonkeynautCommand(new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.MAJOR));

        assertThatThrownBy(() -> createMonkeynaut.create(command))
                .isInstanceOf(NumberOfMonkeynautsExceededException.class)
                .hasMessage("validation.monkeynaut.number-exceeded");
    }

}