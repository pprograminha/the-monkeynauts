package com.themonkeynauts.game.application.usecase;

import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.in.AuthenticateUserCommand;
import com.themonkeynauts.game.application.port.in.AuthenticationUser;
import com.themonkeynauts.game.application.port.out.persistence.LoadUserPort;
import com.themonkeynauts.game.application.port.out.security.TokenUtilsPort;
import com.themonkeynauts.game.common.annotation.UseCaseAdapter;
import com.themonkeynauts.game.common.exception.InvalidCredentialsException;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@UseCaseAdapter
@RequiredArgsConstructor
public class AuthenticationUserUseCase implements AuthenticationUser {

    private final LoadUserPort loadUser;
    private final TokenUtilsPort tokenUtils;
    private final SecurityUtils securityUtils;

    @Override
    public String login(AuthenticateUserCommand command) {
        var user = loadUser.byEmailAndPassword(command.email(), command.password());
        if (user.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        var claims = getClaims(user.get());
        return tokenUtils.generateToken(command.email(), claims);
    }

    @Override
    public User authenticated() {
        return securityUtils.getAuthenticatedUser();
    }

    private Map<String, String> getClaims(User user) {
        var roles = user.getRoles().stream()
                .map(role -> role.name())
                .collect(Collectors.joining(","));
        var claims = new HashMap<String, String>();
        claims.put("userId", user.getId().toString());
        claims.put("email", user.getEmail());
        claims.put("accountId", user.getAccount().getId().toString());
        claims.put("roles", roles);
        return claims;
    }
}
