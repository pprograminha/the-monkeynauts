package com.themonkeynauts.game.adapter.out.security;

import com.themonkeynauts.game.adapter.in.web.rpc.tenant.TenantContext;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.application.port.out.security.SecurityUtilsPort;
import com.themonkeynauts.game.common.exception.InvalidCredentialsException;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SecurityUtils implements SecurityUtilsPort {

    private final UserRepository userRepository;
    private final OutUserMapper userMapper;

    public User getAuthenticatedUser() {
        var userId = getUserId();
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new InvalidCredentialsException();
        }
        return userMapper.toDomain(user.get());
    }

    @Override
    public void setTenant(Account account) {
        TenantContext.setCurrentTenant(account);
    }

    @Override
    public Account getTenant() {
        return TenantContext.currentTenant();
    }

    private UUID getUserId() {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = (String) jwt.getClaims().get("userId");
        return UUID.fromString(userId);
    }
}
