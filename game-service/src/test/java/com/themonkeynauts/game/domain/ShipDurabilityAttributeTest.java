package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.NotEnoughDurabilityException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ShipDurabilityAttributeTest {

    @Test
    public void shouldDecreaseDurability() {
        var durability = new ShipDurabilityAttribute(300);

        durability.decrease(200);

        assertThat(durability.getValue()).isEqualTo(100);
    }

    @Test
    public void shouldThrowExceptionIfDecreasingMoreThanCurrentAmountOfDurability() {
        var durability = new ShipDurabilityAttribute(1);

        assertThatThrownBy(() -> {
            durability.decrease(2);
        })
        .isInstanceOf(NotEnoughDurabilityException.class)
        .hasMessage("validation.ship.not-enough-durability");
    }

}