package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.proto.service.health.HealthCheckRequest;
import com.themonkeynauts.proto.service.health.HealthCheckResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class HealthGrpcTest {

    @InjectMocks
    private HealthGrpc grpc;

    @Mock
    private StreamObserver<HealthCheckResponse> streamObserver;

    @Test
    public void shouldReturnHealthCheckResponse() {
        ReflectionTestUtils.setField(grpc, "response", "UP");
        var request = HealthCheckRequest.newBuilder().build();
        grpc.healthCheck(request, streamObserver);
        verify(streamObserver).onNext(HealthCheckResponse.newBuilder().setResponse("UP").build());
        verify(streamObserver).onCompleted();
    }

}