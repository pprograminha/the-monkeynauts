package com.themonkeynauts.game.adapter.in.web.locale;

import java.util.List;

public class GameLocales {

    private static final String DEFAULT_LOCALE = "en_US";
    private static final String PT_BR = "pt_BR";

    public static final List<String> LOCALES = List.of(DEFAULT_LOCALE, PT_BR);

    public static String getLocale(String headerLang) {
        return LOCALES.stream()
                .filter(locale -> locale.equals(headerLang))
                .findFirst()
                .orElse(DEFAULT_LOCALE);
    }

}
