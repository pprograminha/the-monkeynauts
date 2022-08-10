package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.Ship;

public interface CreateResourceForUser {

    Ship ship(CreateResourceForUserCommand command);

    Monkeynaut monkeynaut(CreateResourceForUserCommand command);
}
