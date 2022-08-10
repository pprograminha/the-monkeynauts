package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.service.authentication.AuthenticatedRequest;
import com.themonkeynauts.proto.service.authentication.AuthenticationGrpc;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
import com.themonkeynauts.proto.service.wallets.CreateWalletRequest;
import com.themonkeynauts.proto.service.wallets.WalletsGrpc;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WalletsGrpcIntegrationTest extends ApplicationContainerTest {

    private WalletsGrpc.WalletsBlockingStub stub;
    private PlayersGrpc.PlayersBlockingStub playerStub;
    private AuthenticationGrpc.AuthenticationBlockingStub authenticationStub;

    private String email = UUID.randomUUID() + "@email.com";
    private String nickname = UUID.randomUUID().toString();
    private String token;
    private String walletId;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        playerStub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));

        createPlayer();

        stub = GrpcStubUtils.addCallWithBearer(WalletsGrpc.newBlockingStub(channel), token);
        authenticationStub = GrpcStubUtils.addCallWithBearer(AuthenticationGrpc.newBlockingStub(channel), token);
    }

    @Test
    @Order(1)
    public void shouldCreateWallet() {
        var walletAddress = "0xb0fB74D6a1F8b454523e649C5DcdF5508d6d4356";
        var request = CreateWalletRequest.newBuilder().setAddress(walletAddress).setName("BNB").build();

        var response = stub.create(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getWallet().getId()).isNotNull();
        assertThat(response.getWallet().getAddress()).isEqualTo(walletAddress);
        assertThat(response.getWallet().getName()).isEqualTo("BNB");

        walletId = response.getWallet().getId();
    }

    @Test
    @Order(2)
    public void shouldReturnWalletInUserResponse() {
        var request = AuthenticatedRequest.newBuilder().build();
        var response = authenticationStub.authenticated(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getUser().getWallet().getId()).isEqualTo(walletId);
    }

    private void createPlayer() {
        var playerRequest = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("PASSWORD").setNickname(nickname).build();

        var playerResponse = playerStub.create(playerRequest);
        assertThat(playerResponse.isInitialized()).isTrue();

        token = playerResponse.getToken();
    }

}
