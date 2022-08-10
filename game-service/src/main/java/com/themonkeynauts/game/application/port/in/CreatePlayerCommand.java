package com.themonkeynauts.game.application.port.in;

public record CreatePlayerCommand(String email, String password, String nickname) {}
