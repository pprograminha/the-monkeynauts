package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.User;

import java.util.Optional;

public interface LoadUserPort {

    Optional<User> byEmailAndPassword(String email, String password);

    Optional<User> byEmail(String email);

    Optional<User> byNickname(String nickname);
}
