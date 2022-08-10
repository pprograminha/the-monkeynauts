package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.ListEquipmentsFromUser;
import com.themonkeynauts.game.application.port.out.persistence.LoadEquipmentPort;
import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;
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
class ListEquipmentsFromUserUseCaseTest {

    private ListEquipmentsFromUser listEquipmentsFromUser;

    @Mock
    private LoadEquipmentPort loadEquipment;

    @BeforeEach
    public void setUp() {
        listEquipmentsFromUser = new ListEquipmentsFromUserUseCase(loadEquipment);
    }

    @Test
    public void shouldListAllEquipments() {
        var equipmentId = UUID.randomUUID();
        var equipment = new Equipment(equipmentId, "Weapon #1", EquipmentType.WEAPON);
        when(loadEquipment.all()).thenReturn(Arrays.asList(equipment));

        var result = listEquipmentsFromUser.all();

        assertThat(result).hasSize(1);
    }

}