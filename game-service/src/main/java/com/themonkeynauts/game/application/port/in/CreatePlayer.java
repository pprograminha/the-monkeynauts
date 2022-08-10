package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.User;

public interface CreatePlayer {

    User create(CreatePlayerCommand command);
}
