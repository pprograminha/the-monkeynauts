package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.service.authentication.AuthenticationGrpc;
import com.themonkeynauts.proto.service.authentication.AuthenticationRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class AuthenticationGrpcIntegrationTest extends ApplicationContainerTest {

    private PlayersGrpc.PlayersBlockingStub playerStub;

    private AuthenticationGrpc.AuthenticationBlockingStub stub;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        playerStub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));
        stub = GrpcStubUtils.addDefaultCall(AuthenticationGrpc.newBlockingStub(channel));
    }

    @Test
    public void shouldCreateAndAuthenticatePlayer() {
        var email = UUID.randomUUID() + "@email.com";
        var nickname = UUID.randomUUID().toString();
        var createRequest = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("password").setNickname(nickname).build();
        var createResponse = playerStub.create(createRequest);
        assertThat(createResponse.isInitialized()).isTrue();

        var request = AuthenticationRequest.newBuilder().setEmail(email).setPassword("password").build();
        var response = stub.authenticate(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getToken()).isNotBlank();
    }

}