package com.themonkeynauts.game.application.port.in;

import java.util.UUID;

public record AddMonkeynautToCrewCommand(UUID shipId,UUID monkeynautId) {}
