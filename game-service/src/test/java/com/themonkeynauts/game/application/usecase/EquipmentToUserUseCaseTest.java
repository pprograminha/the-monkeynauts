package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.application.port.in.EquipmentToUser;
import com.themonkeynauts.game.application.port.in.EquipmentToUserCommand;
import com.themonkeynauts.game.application.port.out.persistence.GrantEquipmentPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadEquipmentPort;
import com.themonkeynauts.game.common.exception.EquipmentNotFoundException;
import com.themonkeynauts.game.domain.Equipment;
import com.themonkeynauts.game.domain.EquipmentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EquipmentToUserUseCaseTest {

    private EquipmentToUser equipmentToUser;

    @Mock
    private LoadEquipmentPort loadEquipment;

    @Mock
    private GrantEquipmentPort grantEquipment;

    @BeforeEach
    public void setUp() {
        equipmentToUser = new EquipmentToUserUseCase(loadEquipment, grantEquipment);
    }

    @Test
    public void shouldGrantEquipment() {
        var equipmentId = UUID.randomUUID();
        var equipment = new Equipment(equipmentId, "Weapon #1", EquipmentType.WEAPON);
        when(loadEquipment.byId(equipmentId)).thenReturn(Optional.of(equipment));

        equipmentToUser.grant(new EquipmentToUserCommand(equipmentId));

        verify(grantEquipment).grant(equipment);
    }

    @Test
    public void shouldThrowExceptionIfEquipmentDoesNotExist() {
        var equipmentId = UUID.randomUUID();
        when(loadEquipment.byId(equipmentId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            equipmentToUser.grant(new EquipmentToUserCommand(equipmentId));
        })
        .isInstanceOf(EquipmentNotFoundException.class)
        .hasMessage("validation.equipment.not-found");
    }

}