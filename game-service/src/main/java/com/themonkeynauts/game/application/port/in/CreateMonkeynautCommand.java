package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.MonkeynautClass;
import com.themonkeynauts.game.domain.MonkeynautRank;

public record CreateMonkeynautCommand(MonkeynautClass clazz, MonkeynautRank rank) {}
