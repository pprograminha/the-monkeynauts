package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautClassTest {

    @Test
    public void shouldCreateEngineerClass() {
        var clazz = new MonkeynautClass(MonkeynautClassType.ENGINEER);

        assertThat(clazz.isEngineer()).isTrue();
    }

    @Test
    public void shouldCreateScientistClass() {
        var clazz = new MonkeynautClass(MonkeynautClassType.SCIENTIST);

        assertThat(clazz.isScientist()).isTrue();
    }

    @Test
    public void shouldCreateSoldierClass() {
        var clazz = new MonkeynautClass(MonkeynautClassType.SOLDIER);

        assertThat(clazz.isSoldier()).isTrue();
    }

    @Test
    public void shouldBeEqualByClassType() {
        var class1 = new MonkeynautClass(MonkeynautClassType.SOLDIER);
        var class2 = new MonkeynautClass(MonkeynautClassType.SOLDIER);

        assertThat(class1).isEqualTo(class2);
    }

    @Test
    public void shouldThrowExceptionIfTypeIsNull() {
        assertThatThrownBy(() -> new MonkeynautClass(null))
        .isInstanceOf(MonkeynautRequiredAttributeException.class)
        .hasMessage("validation.required.monkeynaut");
    }

}