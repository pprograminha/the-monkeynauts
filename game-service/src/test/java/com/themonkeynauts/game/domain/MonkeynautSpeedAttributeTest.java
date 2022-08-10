package com.themonkeynauts.game.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautSpeedAttributeTest {

    @Test
    public void shouldThrowExceptionIfValueIsLesserThanLowLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautSpeedAttribute(19);
        });
    }

    @Test
    public void shouldThrowExceptionIfValueIsGreaterThanHighLimit() {
        assertThatThrownBy(() -> {
            new MonkeynautSpeedAttribute(51);
        });
    }

    @Test
    public void shouldCreateSpeedAttribute() {
        var attribute = new MonkeynautSpeedAttribute(50);

        assertThat(attribute.getValue()).isEqualTo(50);
    }

}