package com.themonkeynauts.game.application.port.out.persistence;

import com.themonkeynauts.game.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SaveUserPort {

    User save(User player);

    void startBountyHuntingQuest(UUID userId, LocalDateTime time);
}
