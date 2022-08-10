package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.service.health.HealthCheckRequest;
import com.themonkeynauts.proto.service.health.HealthCheckResponse;
import com.themonkeynauts.proto.service.health.HealthGrpc;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class HealthGrpcIntegrationTest extends ApplicationContainerTest {

    private HealthGrpc.HealthBlockingStub stub;

    @BeforeEach
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        stub = GrpcStubUtils.addDefaultCall(HealthGrpc.newBlockingStub(channel));
    }

    @Test
    public void shouldBeUp() {
        var response = stub.healthCheck(HealthCheckRequest.newBuilder().build());
        assertThat(response).isEqualTo(HealthCheckResponse.newBuilder().setResponse("YAY").build());
    }
}
