package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.common.messages.Monkeynaut;
import com.themonkeynauts.proto.common.messages.Ship;
import com.themonkeynauts.proto.common.messages.ShipClass;
import com.themonkeynauts.proto.common.messages.ShipRank;
import com.themonkeynauts.proto.service.monkeynauts.CreateMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.MonkeynautsGrpc;
import com.themonkeynauts.proto.service.monkeynauts.RandomMonkeynautGenerationRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
import com.themonkeynauts.proto.service.ships.AddMonkeynautToCrewRequest;
import com.themonkeynauts.proto.service.ships.CreateShipRequest;
import com.themonkeynauts.proto.service.ships.ListShipRequest;
import com.themonkeynauts.proto.service.ships.RandomShipGenerationRequest;
import com.themonkeynauts.proto.service.ships.RemoveMonkeynautFromCrewRequest;
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

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShipsGrpcIntegrationTest extends ApplicationContainerTest {

    private ShipsGrpc.ShipsBlockingStub stub;
    private PlayersGrpc.PlayersBlockingStub playerStub;
    private MonkeynautsGrpc.MonkeynautsBlockingStub monkeynautsStub;

    private String email = UUID.randomUUID() + "@email.com";
    private String nickname = UUID.randomUUID().toString();
    private String token;
    private Ship ship;
    private List<Monkeynaut> monkeynauts;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        playerStub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));

        createPlayer();

        stub = GrpcStubUtils.addCallWithBearer(ShipsGrpc.newBlockingStub(channel), token);
        monkeynautsStub = GrpcStubUtils.addCallWithBearer(MonkeynautsGrpc.newBlockingStub(channel), token);

        createMonkeynauts();
    }

    @Test
    @Order(1)
    public void shouldCreateRandomShip() {
        var random = RandomShipGenerationRequest.newBuilder().build();
        var request = CreateShipRequest.newBuilder().setRandom(random).build();

        var response = stub.create(request);

        assertThat(response.isInitialized()).isTrue();
        ship = response.getShip();

        assertThat(response.getShip().getId()).isNotNull();
        assertThat(response.getShip().getRank()).isIn(ShipRank.B, ShipRank.A, ShipRank.S);
        assertThat(response.getShip().getClass_()).isIn(ShipClass.MINER, ShipClass.FIGHTER, ShipClass.EXPLORER);
    }

    @Test
    @Order(2)
    public void shouldAddMonkeynautsToCrew() {
        var requestAddMonkeynaut1ToCrew = AddMonkeynautToCrewRequest.newBuilder().setShipId(ship.getId()).setMonkeynautId(monkeynauts.get(0).getId()).build();
        var requestAddMonkeynaut2ToCrew = AddMonkeynautToCrewRequest.newBuilder().setShipId(ship.getId()).setMonkeynautId(monkeynauts.get(1).getId()).build();

        var responseAddMonkeynaut1ToCrew = stub.addMonkeynautToCrew(requestAddMonkeynaut1ToCrew);
        var responseAddMonkeynaut2ToCrew = stub.addMonkeynautToCrew(requestAddMonkeynaut2ToCrew);

        assertThat(responseAddMonkeynaut1ToCrew.isInitialized()).isTrue();
        assertThat(responseAddMonkeynaut2ToCrew.isInitialized()).isTrue();

        var listShipsResponse = stub.list(ListShipRequest.newBuilder().build());
        assertThat(listShipsResponse.isInitialized()).isTrue();

        assertThat(listShipsResponse.getShipsList().get(0).getCrew().getMonkeynautsList()).hasSize(2);
        assertThat(listShipsResponse.getShipsList().get(0).getCrew().getMonkeynautsList().get(0).getId()).isEqualTo(monkeynauts.get(0).getId());
        assertThat(listShipsResponse.getShipsList().get(0).getCrew().getMonkeynautsList().get(1).getId()).isEqualTo(monkeynauts.get(1).getId());
    }

    @Test
    @Order(3)
    public void shouldRemoveMonkeynautFromCrew() {
        var removeMonkeynautRequest = RemoveMonkeynautFromCrewRequest.newBuilder().setShipId(ship.getId()).setMonkeynautId(monkeynauts.get(0).getId()).build();
        var responseRemoveMonkeynaut = stub.removeMonkeynautFromCrew(removeMonkeynautRequest);

        assertThat(responseRemoveMonkeynaut.isInitialized()).isTrue();

        var listShipsResponse = stub.list(ListShipRequest.newBuilder().build());
        assertThat(listShipsResponse.isInitialized()).isTrue();

        assertThat(listShipsResponse.getShipsList().get(0).getCrew().getMonkeynautsList()).hasSize(1);
    }

    private void createPlayer() {
        var playerRequest = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("PASSWORD").setNickname(nickname).build();

        var playerResponse = playerStub.create(playerRequest);
        assertThat(playerResponse.isInitialized()).isTrue();

        token = playerResponse.getToken();
    }

    private void createMonkeynauts() {
        var random = RandomMonkeynautGenerationRequest.newBuilder().build();
        var createMonkeynautRequest = CreateMonkeynautRequest.newBuilder().setRandom(random).build();

        var monkeynaut1Response = monkeynautsStub.create(createMonkeynautRequest);
        var monkeynaut2Response = monkeynautsStub.create(createMonkeynautRequest);
        assertThat(monkeynaut1Response.isInitialized()).isTrue();
        assertThat(monkeynaut2Response.isInitialized()).isTrue();

        monkeynauts = List.of(monkeynaut1Response.getMonkeynaut(), monkeynaut2Response.getMonkeynaut());
    }
}
