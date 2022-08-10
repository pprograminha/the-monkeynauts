package com.themonkeynauts.game.adapter.in.web.locale;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GameLocalesTest {

    @Test
    public void shouldGetDefaultLocale() {
        var result = GameLocales.getLocale("no_LN");

        assertThat(result).isEqualTo("en_US");
    }

    @Test
    public void shouldGetPtBrLocale() {
        var result = GameLocales.getLocale("pt_BR");

        assertThat(result).isEqualTo("pt_BR");
    }

}