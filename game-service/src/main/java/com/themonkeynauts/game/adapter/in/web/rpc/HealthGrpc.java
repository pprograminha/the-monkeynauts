package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.health.HealthCheckRequest;
import com.themonkeynauts.proto.service.health.HealthCheckResponse;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

@PresentationAdapter
public class HealthGrpc extends com.themonkeynauts.proto.service.health.HealthGrpc.HealthImplBase {

    private static final Logger logger = LoggerFactory.getLogger(HealthGrpc.class);

    @Value("${application.health.response}")
    private String response;

    @Override
    public void healthCheck(HealthCheckRequest request, StreamObserver<HealthCheckResponse> responseObserver) {
        logger.info("Application health response: {}", response);
        responseObserver.onNext(HealthCheckResponse.newBuilder().setResponse(response).build());
        responseObserver.onCompleted();
    }
}
