package com.themonkeynauts.game.adapter.in.web.rpc.interceptor;

import com.themonkeynauts.game.adapter.in.web.locale.GameLocales;
import com.themonkeynauts.game.adapter.in.web.rpc.context.GameContexts;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.apache.commons.lang3.StringUtils;

import java.util.Locale;

@GrpcGlobalServerInterceptor
public class LocaleInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call, Metadata headers, ServerCallHandler<ReqT, RespT> next) {
        var locale = Locale.getDefault();

        if (!StringUtils.isBlank(headers.get(GameContexts.LOCALE_METADATA_KEY))) {
            locale = new Locale(GameLocales.getLocale(headers.get(GameContexts.LOCALE_METADATA_KEY)));
        }

        var newContext = Context.current().withValue(GameContexts.LOCALE_CONTEXT_KEY, locale);

        return Contexts.interceptCall(newContext, call, headers, next);
    }
}
