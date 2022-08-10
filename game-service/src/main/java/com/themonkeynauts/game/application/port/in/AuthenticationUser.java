package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.User;

public interface AuthenticationUser {

    String login(AuthenticateUserCommand command);

    User authenticated();
}
