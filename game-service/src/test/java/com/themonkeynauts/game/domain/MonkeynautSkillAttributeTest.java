package com.themonkeynauts.game.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautSkillAttributeTest {

    @Test
    public void shouldThrowExceptionIfValueIsLesserThanLowLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautSkillAttribute(19);
        });
    }

    @Test
    public void shouldThrowExceptionIfValueIsGreaterThanHighLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautSkillAttribute(51);
        });
    }

    @Test
    public void shouldCreateSkillAttribute() {
        var attribute = new MonkeynautSkillAttribute(20);

        assertThat(attribute.getValue()).isEqualTo(20);
    }

}