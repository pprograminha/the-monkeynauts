package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.proto.common.messages.MonkeynautClass;
import com.themonkeynauts.proto.common.messages.MonkeynautRank;
import com.themonkeynauts.proto.service.authentication.AuthenticationGrpc;
import com.themonkeynauts.proto.service.authentication.AuthenticationRequest;
import com.themonkeynauts.proto.service.monkeynauts.CreateMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.ListMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.MonkeynautsGrpc;
import com.themonkeynauts.proto.service.monkeynauts.RandomMonkeynautGenerationRequest;
import com.themonkeynauts.proto.service.monkeynauts.SpecifiedMonkeynautGenerationRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
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
public class MonkeynautsGrpcIntegrationTest extends ApplicationContainerTest {

    private MonkeynautsGrpc.MonkeynautsBlockingStub stub;
    private PlayersGrpc.PlayersBlockingStub playerStub;
    private AuthenticationGrpc.AuthenticationBlockingStub authenticateStub;

    private String email = UUID.randomUUID() + "@email.com";
    private String nickname = UUID.randomUUID().toString();
    private String token;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        playerStub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));
        authenticateStub = GrpcStubUtils.addDefaultCall(AuthenticationGrpc.newBlockingStub(channel));

        createPlayer();
        authenticatePlayer();

        stub = GrpcStubUtils.addCallWithBearer(MonkeynautsGrpc.newBlockingStub(channel), token);
    }

    private void createPlayer() {
        var playerRequest = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("PASSWORD").setNickname(nickname).build();

        var playerResponse = playerStub.create(playerRequest);
        assertThat(playerResponse.isInitialized()).isTrue();
    }

    private void authenticatePlayer() {
        var authenticateRequest = AuthenticationRequest.newBuilder().setEmail(email).setPassword("PASSWORD").build();
        var authenticateResponse = authenticateStub.authenticate(authenticateRequest);
        assertThat(authenticateResponse.isInitialized()).isTrue();

        token = authenticateResponse.getToken();
    }

    @Test
    @Order(1)
    public void shouldCreateSpecifiedMonkeynaut() {
        var specified = SpecifiedMonkeynautGenerationRequest.newBuilder().setClass_(MonkeynautClass.SCIENTIST).setRank(MonkeynautRank.CAPTAIN).build();
        var request = CreateMonkeynautRequest.newBuilder().setSpecified(specified).build();

        var response = stub.create(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getMonkeynaut().getId()).isNotNull();
        assertThat(response.getMonkeynaut().getNumber()).isNotNull();
        assertThat(response.getMonkeynaut().getFirstName()).isNotNull();
        assertThat(response.getMonkeynaut().getLastName()).isNotNull();
        assertThat(response.getMonkeynaut().getClass_()).isEqualTo(MonkeynautClass.SCIENTIST);
        assertThat(response.getMonkeynaut().getRank()).isEqualTo(MonkeynautRank.CAPTAIN);
        assertThat(response.getMonkeynaut().getAttributes().getSkill()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getSpeed()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getResistance()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getLife()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getCurrentEnergy()).isEqualTo(6);
        assertThat(response.getMonkeynaut().getAttributes().getMaxEnergy()).isEqualTo(6);
    }

    @Test
    @Order(2)
    public void shouldCreateRandomMonkeynaut() {
        var random = RandomMonkeynautGenerationRequest.newBuilder().build();
        var request = CreateMonkeynautRequest.newBuilder().setRandom(random).build();
        var response = stub.create(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getMonkeynaut().getId()).isNotNull();
        assertThat(response.getMonkeynaut().getNumber()).isNotNull();
        assertThat(response.getMonkeynaut().getFirstName()).isNotNull();
        assertThat(response.getMonkeynaut().getLastName()).isNotNull();
        assertThat(response.getMonkeynaut().getClass_()).isIn(MonkeynautClass.SCIENTIST, MonkeynautClass.ENGINEER, MonkeynautClass.SOLDIER);
        assertThat(response.getMonkeynaut().getRank()).isIn(MonkeynautRank.PRIVATE, MonkeynautRank.SERGEANT, MonkeynautRank.CAPTAIN, MonkeynautRank.MAJOR);
        assertThat(response.getMonkeynaut().getAttributes().getSkill()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getSpeed()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getResistance()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getLife()).isNotNull();
        assertThat(response.getMonkeynaut().getAttributes().getCurrentEnergy()).isGreaterThanOrEqualTo(2);
        assertThat(response.getMonkeynaut().getAttributes().getCurrentEnergy()).isLessThanOrEqualTo(8);
        assertThat(response.getMonkeynaut().getAttributes().getMaxEnergy()).isGreaterThanOrEqualTo(2);
        assertThat(response.getMonkeynaut().getAttributes().getMaxEnergy()).isLessThanOrEqualTo(8);
    }

    @Test
    @Order(3)
    public void shouldReturnListOfMonkeynauts() {
        var request = ListMonkeynautRequest.newBuilder().build();
        var response = stub.list(request);

        assertThat(response.isInitialized()).isTrue();
        assertThat(response.getMonkeynautsList()).hasSize(2);
    }

}
