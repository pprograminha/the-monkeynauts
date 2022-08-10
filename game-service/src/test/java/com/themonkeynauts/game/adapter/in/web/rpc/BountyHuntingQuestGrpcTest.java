package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautClassMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautRankMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InRoleMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipClassMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipRankMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuest;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuestCommand;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.ShipAttributes;
import com.themonkeynauts.game.domain.ShipClass;
import com.themonkeynauts.game.domain.ShipClassType;
import com.themonkeynauts.game.domain.ShipDurabilityAttribute;
import com.themonkeynauts.game.domain.ShipRank;
import com.themonkeynauts.game.domain.ShipRankType;
import com.themonkeynauts.proto.common.messages.ShipBonus;
import com.themonkeynauts.proto.common.messages.ShipCrew;
import com.themonkeynauts.proto.service.bountyhuntingquest.StartBountyHuntingQuestRequest;
import com.themonkeynauts.proto.service.bountyhuntingquest.StartBountyHuntingQuestResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BountyHuntingQuestGrpcTest {

    private BountyHuntingQuestGrpc grpc;

    @Mock
    private StartBountyHuntingQuest startBountyHuntingQuest;

    @Mock
    private StreamObserver<StartBountyHuntingQuestResponse> startResponseStreamObserver;

    @BeforeEach
    public void setUp() {
        var userMapper = new InUserMapper(new InRoleMapper(), new InWalletMapper());
        var monkeynautMapper = new InMonkeynautMapper(new InMonkeynautClassMapper(), new InMonkeynautRankMapper(), userMapper);
        grpc = new BountyHuntingQuestGrpc(startBountyHuntingQuest, new InShipMapper(new InShipClassMapper(), new InShipRankMapper(), monkeynautMapper, userMapper));
    }

    @Test
    public void shouldStartBountyHuntingQuest() {
        var shipId = UUID.randomUUID();
        var ship = new Ship(shipId, "Name", new ShipClass(ShipClassType.MINER), new ShipRank(ShipRankType.S), new ShipAttributes(new ShipDurabilityAttribute(50)));
        when(startBountyHuntingQuest.with(new StartBountyHuntingQuestCommand(shipId))).thenReturn(ship);

        var request = StartBountyHuntingQuestRequest.newBuilder().setShipId(shipId.toString()).build();
        grpc.start(request, startResponseStreamObserver);

        var protoShip = com.themonkeynauts.proto.common.messages.Ship.newBuilder()
                .setId(shipId.toString())
                .setName("Name")
                .setClass_(com.themonkeynauts.proto.common.messages.ShipClass.MINER)
                .setRank(com.themonkeynauts.proto.common.messages.ShipRank.S)
                .setAttributes(com.themonkeynauts.proto.common.messages.ShipAttributes.newBuilder().setCurrentDurability(50).setMaxDurability(400).build())
                .setBonus(ShipBonus.newBuilder().setDescription("Mining Mission Success Rate").setValue(40f).build())
                .setCrew(ShipCrew.newBuilder().setSeats(4).build())
                .build();
        verify(startResponseStreamObserver).onNext(StartBountyHuntingQuestResponse.newBuilder().setShip(protoShip).build());
        verify(startResponseStreamObserver).onCompleted();
    }

}