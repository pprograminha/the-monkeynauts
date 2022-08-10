package com.themonkeynauts.game.adapter.in.web.rpc.context;

import com.themonkeynauts.game.domain.Account;
import io.grpc.Context;
import io.grpc.Metadata;

import java.util.Locale;

public class GameContexts {

    public static final Metadata.Key<String> LOCALE_METADATA_KEY = Metadata.Key.of("x-the-monkeynauts-locale", Metadata.ASCII_STRING_MARSHALLER);
    public static final Context.Key<Locale> LOCALE_CONTEXT_KEY = Context.key("locale");

    public static final Metadata.Key<String> BEARER_METADATA_KEY = Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER);

    public static final Context.Key<Account> TENANT_CONTEXT_KEY = Context.key("tenant");
}
