package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.Monkeynaut;

public interface CreateMonkeynaut {

    Monkeynaut createRandom();

    Monkeynaut create(CreateMonkeynautCommand command);
}
