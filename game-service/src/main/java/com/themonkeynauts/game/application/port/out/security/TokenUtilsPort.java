package com.themonkeynauts.game.application.port.out.security;

import java.util.Map;

public interface TokenUtilsPort {

    String generateToken(String username, Map<String, String> claims);

}
