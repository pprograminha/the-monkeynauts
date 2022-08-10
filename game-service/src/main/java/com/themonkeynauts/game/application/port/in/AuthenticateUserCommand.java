package com.themonkeynauts.game.application.port.in;

public record AuthenticateUserCommand(String email, String password) {}
