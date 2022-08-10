package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.ListMonkeynaut;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.MonkeynautAttributes;
import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautClassType;
import com.themonkeynauts.game.domain.MonkeynautSkillAttribute;
import com.themonkeynauts.game.domain.MonkeynautEnergyAttribute;
import com.themonkeynauts.game.domain.MonkeynautLifeAttribute;
import com.themonkeynauts.game.domain.MonkeynautRank;
import com.themonkeynauts.game.domain.MonkeynautRankType;
import com.themonkeynauts.game.domain.MonkeynautResistanceAttribute;
import com.themonkeynauts.game.domain.MonkeynautSpeedAttribute;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListMonkeynautUseCaseTest {

    private ListMonkeynaut listMonkeynaut;

    @Mock
    private LoadMonkeynautPort loadMonkeynaut;

    @BeforeEach
    public void setUp() {
        listMonkeynaut = new ListMonkeynautUseCase(loadMonkeynaut);
    }

    @Test
    public void shouldListAllMonkeynauts() {
        var id = UUID.randomUUID();
        var attributes = new MonkeynautAttributes(new MonkeynautSkillAttribute(50), new MonkeynautSpeedAttribute(40), new MonkeynautResistanceAttribute(30), new MonkeynautLifeAttribute(250), new MonkeynautEnergyAttribute(6));
        var monkeynaut = new Monkeynaut(id, "Firstname", "Lastname", new MonkeynautClass(MonkeynautClassType.ENGINEER), new MonkeynautRank(MonkeynautRankType.CAPTAIN), attributes);
        var monkeynauts = Arrays.asList(monkeynaut);
        when(loadMonkeynaut.all()).thenReturn(monkeynauts);

        var result = listMonkeynaut.all();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(id);
    }

}