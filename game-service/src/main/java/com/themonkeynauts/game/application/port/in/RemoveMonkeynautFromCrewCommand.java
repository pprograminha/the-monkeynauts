package com.themonkeynauts.game.application.port.in;

import java.util.UUID;

public record RemoveMonkeynautFromCrewCommand(UUID shipId, UUID monkeynautId) {}
