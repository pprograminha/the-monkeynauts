package com.themonkeynauts.game.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautLifeAttributeTest {

    @Test
    public void shouldThrowExceptionIfValueIsLesserThanLowLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautLifeAttribute(249);
        });
    }

    @Test
    public void shouldThrowExceptionIfValueIsGreaterThanHighLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautLifeAttribute(351);
        });
    }

    @Test
    public void shouldCreateLifeAttribute() {
        var attribute = new MonkeynautLifeAttribute(350);

        assertThat(attribute.getValue()).isEqualTo(350);
    }

}