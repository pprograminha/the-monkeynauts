package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.rpc.context.GameContexts;
import io.grpc.CallCredentials;
import io.grpc.Metadata;

import java.util.concurrent.Executor;

public class GrpcStubUtils {

    protected static final String DEFAULT_LOCALE = "en_US";

    public static <T extends io.grpc.stub.AbstractStub<T>> T addDefaultCall(T stub) {
        return addCallWithLocale(stub, GrpcStubUtils.DEFAULT_LOCALE);
    }

    public static <T extends io.grpc.stub.AbstractStub<T>> T addCallWithLocale(T stub, String locale) {
        return stub.withCallCredentials(new CallCredentials() {
            @Override
            public void applyRequestMetadata(CallCredentials.RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
                Metadata metadata = new Metadata();
                metadata.put(GameContexts.LOCALE_METADATA_KEY, locale);
                applier.apply(metadata);
            }

            @Override
            public void thisUsesUnstableApi() {
            }
        });
    }

    public static <T extends io.grpc.stub.AbstractStub<T>> T addCallWithBearer(T stub, String bearer) {
        return stub.withCallCredentials(new CallCredentials() {
            @Override
            public void applyRequestMetadata(CallCredentials.RequestInfo requestInfo, Executor appExecutor, MetadataApplier applier) {
                Metadata metadata = new Metadata();
                metadata.put(GameContexts.LOCALE_METADATA_KEY, DEFAULT_LOCALE);
                metadata.put(GameContexts.BEARER_METADATA_KEY, "Bearer %s".formatted(bearer));
                applier.apply(metadata);
            }

            @Override
            public void thisUsesUnstableApi() {
            }
        });
    }
}
