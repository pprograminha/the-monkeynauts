package com.themonkeynauts.game.domain;

import com.themonkeynauts.game.common.exception.MonkeynautRequiredAttributeException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MonkeynautRankTest {

    @Test
    public void shouldCreatePrivateRank() {
        var rank = new MonkeynautRank(MonkeynautRankType.PRIVATE);

        assertThat(rank.isPrivate()).isTrue();
    }

    @Test
    public void shouldCreateSergeantRank() {
        var rank = new MonkeynautRank(MonkeynautRankType.SERGEANT);

        assertThat(rank.isSergeant()).isTrue();
    }

    @Test
    public void shouldCreateCaptainRank() {
        var rank = new MonkeynautRank(MonkeynautRankType.CAPTAIN);

        assertThat(rank.isCaptain()).isTrue();
    }

    @Test
    public void shouldCreateMajorRank() {
        var rank = new MonkeynautRank(MonkeynautRankType.MAJOR);

        assertThat(rank.isMajor()).isTrue();
    }

    @Test
    public void shouldBeEqualByRankType() {
        var rank1 = new MonkeynautRank(MonkeynautRankType.MAJOR);
        var rank2 = new MonkeynautRank(MonkeynautRankType.MAJOR);

        assertThat(rank1).isEqualTo(rank2);
    }

    @Test
    public void shouldThrowExceptionIfTypeIsNull() {
        assertThatThrownBy(() -> new MonkeynautRank(null))
        .isInstanceOf(MonkeynautRequiredAttributeException.class)
        .hasMessage("validation.required.monkeynaut");
    }

}