package com.themonkeynauts.game.adapter.in.web.rpc.interceptor;

import com.themonkeynauts.game.adapter.in.web.rpc.context.GameContexts;
import com.themonkeynauts.game.domain.Account;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.UUID;

@GrpcGlobalServerInterceptor
public class TenantInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        if (StringUtils.isBlank(headers.get(GameContexts.BEARER_METADATA_KEY))) {
            return next.startCall(call, headers);
        }

        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var accountId = (String) jwt.getClaims().get("accountId");
        var tenant = new Account(UUID.fromString(accountId));
        var newContext = Context.current().withValue(GameContexts.TENANT_CONTEXT_KEY, tenant);

        return Contexts.interceptCall(newContext, call, headers, next);
    }
}
