package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.common.messages.Role;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
class PlayersGrpcIntegrationTest extends ApplicationContainerTest {

    private PlayersGrpc.PlayersBlockingStub stub;

    @BeforeEach
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        stub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));
    }

    @Test
    public void shouldCreatePlayer() {
        var email = UUID.randomUUID() + "@email.com";
        var nickname = UUID.randomUUID().toString();
        var request = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("PASSWORD").setNickname(nickname).build();

        var response = stub.create(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getPlayer().getId()).isNotNull();
        assertThat(response.getPlayer().getEmail()).isEqualTo(email);
        assertThat(response.getPlayer().getNickname()).isEqualTo(nickname);
        assertThat(response.getPlayer().getRolesList()).contains(Role.ADMIN, Role.PLAYER);
        assertThat(response.getToken()).isNotNull();
    }

}