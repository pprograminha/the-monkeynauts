package com.themonkeynauts.game.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautResistanceAttributeTest {

    @Test
    public void shouldThrowExceptionIfValueIsLesserThanLowLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautResistanceAttribute(19);
        });
    }

    @Test
    public void shouldThrowExceptionIfValueIsGreaterThanHighLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautResistanceAttribute(51);
        });
    }

    @Test
    public void shouldCreateSkillAttribute() {
        var attribute = new MonkeynautResistanceAttribute(20);

        assertThat(attribute.getValue()).isEqualTo(20);
    }

}