package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.ApplicationContainerTest;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.EquipmentEnum;
import com.themonkeynauts.proto.service.authentication.AuthenticationGrpc;
import com.themonkeynauts.proto.service.authentication.AuthenticationRequest;
import com.themonkeynauts.proto.service.equipments.EquipmentsGrpc;
import com.themonkeynauts.proto.service.equipments.GrantToUserEquipmentRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.PlayersGrpc;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = { "grpc.server.port=" + ApplicationContainerTest.RANDOM_GRPC_PORT })
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class ListEquipmentsGrpcIntegrationTest extends ApplicationContainerTest {

    private EquipmentsGrpc.EquipmentsBlockingStub stub;
    private PlayersGrpc.PlayersBlockingStub playerStub;
    private AuthenticationGrpc.AuthenticationBlockingStub authenticateStub;

    private String email = UUID.randomUUID() + "@email.com";
    private String nickname = UUID.randomUUID().toString();
    private String token;

    private EquipmentEntity weapon;
    private EquipmentEntity shield;
    private EquipmentEntity accessory;

    @BeforeAll
    public void init() {
        channel = ManagedChannelBuilder.forAddress("localhost", properties.getPort()).usePlaintext().build();
        playerStub = GrpcStubUtils.addDefaultCall(PlayersGrpc.newBlockingStub(channel));
        authenticateStub = GrpcStubUtils.addDefaultCall(AuthenticationGrpc.newBlockingStub(channel));

        createPlayer();
        authenticatePlayer();

        stub = GrpcStubUtils.addCallWithBearer(EquipmentsGrpc.newBlockingStub(channel), token);

        createEquipments();
    }

    private void createPlayer() {
        var playerRequest = CreatePlayerRequest.newBuilder().setEmail(email).setPassword("PASSWORD").setNickname(nickname).build();

        var playerResponse = playerStub.create(playerRequest);
        assertThat(playerResponse.isInitialized()).isTrue();
    }

    private void authenticatePlayer() {
        var request = AuthenticationRequest.newBuilder().setEmail(email).setPassword("PASSWORD").build();
        var response = authenticateStub.authenticate(request);
        assertThat(response.isInitialized()).isTrue();

        token = response.getToken();
    }

    private void createEquipments() {
        var weapon = EquipmentEntity.builder().name("Weapon #1").type(EquipmentEnum.WEAPON).build();
        var shield = EquipmentEntity.builder().name("Shield #1").type(EquipmentEnum.SHIELD).build();
        var accessory = EquipmentEntity.builder().name("Accessory #1").type(EquipmentEnum.ACCESSORY).build();

        this.weapon = equipmentRepository.save(weapon);
        this.shield = equipmentRepository.save(shield);
        this.accessory = equipmentRepository.save(accessory);
    }

    @Test
    @Order(1)
    public void shouldGrantEquipmentToUser() {
        var request = GrantToUserEquipmentRequest.newBuilder().setEquipmentId(weapon.getId().toString()).build();

        var response = stub.grantToUser(request);

        assertThat(response.isInitialized()).isTrue();
    }
}
