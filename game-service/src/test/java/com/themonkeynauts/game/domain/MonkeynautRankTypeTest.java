package com.themonkeynauts.game.domain;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.stream;
import static org.assertj.core.api.Assertions.assertThat;

class MonkeynautRankTypeTest {

    @Test
    public void shouldHaveOneHundredPercentOfTotalChances() {
        var chances = stream(MonkeynautRankType.values())
                .map(type -> type.chance())
                .reduce(0, (a, b) -> a + b);

        assertThat(chances).isEqualTo(100);
    }

}