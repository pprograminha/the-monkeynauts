package com.themonkeynauts.game.application.port.in;

import com.themonkeynauts.game.domain.Ship;

public interface StartBountyHuntingQuest {

    Ship with(StartBountyHuntingQuestCommand command);
}
