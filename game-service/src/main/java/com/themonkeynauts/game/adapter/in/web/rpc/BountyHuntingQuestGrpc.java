package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InShipMapper;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuest;
import com.themonkeynauts.game.application.port.in.StartBountyHuntingQuestCommand;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.bountyhuntingquest.StartBountyHuntingQuestRequest;
import com.themonkeynauts.proto.service.bountyhuntingquest.StartBountyHuntingQuestResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

@PresentationAdapter
@RequiredArgsConstructor
public class BountyHuntingQuestGrpc extends com.themonkeynauts.proto.service.bountyhuntingquest.BountyHuntingQuestGrpc.BountyHuntingQuestImplBase {

    private final StartBountyHuntingQuest startBountyHuntingQuest;
    private final InShipMapper mapper;

    @Override
    @Secured({ "ROLE_PLAYER" })
    public void start(StartBountyHuntingQuestRequest request, StreamObserver<StartBountyHuntingQuestResponse> responseObserver) {
        var command = new StartBountyHuntingQuestCommand(UUID.fromString(request.getShipId()));
        var updatedShip = startBountyHuntingQuest.with(command);
        responseObserver.onNext(StartBountyHuntingQuestResponse.newBuilder().setShip(mapper.toProto(updatedShip)).build());
        responseObserver.onCompleted();
    }
}
