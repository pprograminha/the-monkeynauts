package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.common.messages.Ship;
import com.themonkeynauts.proto.service.authentication.AuthenticatedRequest;
import com.themonkeynauts.proto.service.authentication.AuthenticationGrpc;
import com.themonkeynauts.proto.service.bountyhuntingquest.BountyHuntingQuestGrpc;
import com.themonkeynauts.proto.service.bountyhuntingquest.StartBountyHuntingQuestRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
import com.themonkeynauts.proto.service.ships.CreateShipRequest;
import com.themonkeynauts.proto.service.ships.RandomShipGenerationRequest;
import com.themonkeynauts.proto.service.ships.ShipsGrpc;
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
public class BountyHuntingQuestGrpcIntegrationTest extends ApplicationContainerTest {

    private BountyHuntingQuestGrpc.BountyHuntingQuestBlockingStub stub;
    private PlayersGrpc.PlayersBlockingStub playerStub;
    private ShipsGrpc.ShipsBlockingStub shipStub;
    private AuthenticationGrpc.AuthenticationBlockingStub authenticationStub;

    private String email = UUID.randomUUID() + "@email.com";
    private String nickname = UUID.randomUUID().toString();
    private String token;
    private Ship ship;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();

        playerStub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));
        createPlayer();

        shipStub = GrpcStubUtils.addCallWithBearer(ShipsGrpc.newBlockingStub(channel), token);
        createShip();

        authenticationStub = GrpcStubUtils.addCallWithBearer(AuthenticationGrpc.newBlockingStub(channel), token);

        stub = GrpcStubUtils.addCallWithBearer(BountyHuntingQuestGrpc.newBlockingStub(channel), token);
    }

    @Test
    @Order(1)
    public void shouldDecreaseDurabilityBy200WhenStartingBountyHuntingQuest() {
        var currentDurability = ship.getAttributes().getCurrentDurability();

        var request = StartBountyHuntingQuestRequest.newBuilder().setShipId(ship.getId()).build();
        var response = stub.start(request);

        assertThat(response.getShip().getAttributes().getCurrentDurability()).isEqualTo(currentDurability - 100);
    }

    @Test
    @Order(2)
    public void shouldSetLastBountyHuntingDateTimeInLoggedPlayer() {
        var request = AuthenticatedRequest.newBuilder().build();
        var response = authenticationStub.authenticated(request);

        assertThat(response.getUser().getLastBountyHuntingDateTime()).isNotNull();
    }

    private void createShip() {
        var random = RandomShipGenerationRequest.newBuilder().build();
        var request = CreateShipRequest.newBuilder().setRandom(random).build();

        var response = shipStub.create(request);

        assertThat(response.isInitialized()).isTrue();
        ship = response.getShip();
    }

    private void createPlayer() {
        var playerRequest = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("PASSWORD").setNickname(nickname).build();

        var playerResponse = playerStub.create(playerRequest);
        assertThat(playerResponse.isInitialized()).isTrue();

        token = playerResponse.getToken();
    }
}
