package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InShipMapper;
import com.themonkeynauts.game.application.port.in.CreateResourceForUser;
import com.themonkeynauts.game.application.port.in.CreateResourceForUserCommand;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.admin.AdminCreateMonkeynautRequest;
import com.themonkeynauts.proto.service.admin.AdminCreateMonkeynautResponse;
import com.themonkeynauts.proto.service.admin.AdminCreateShipRequest;
import com.themonkeynauts.proto.service.admin.AdminCreateShipResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

@PresentationAdapter
@RequiredArgsConstructor
public class AdminResourcesGrpc extends com.themonkeynauts.proto.service.admin.AdminResourcesGrpc.AdminResourcesImplBase {

    private final CreateResourceForUser createResourceForUser;
    private final InShipMapper shipMapper;
    private final InMonkeynautMapper monkeynautMapper;

    @Override
    @Secured({ "ROLE_ROOT" })
    public void createRandomShip(AdminCreateShipRequest request, StreamObserver<AdminCreateShipResponse> responseObserver) {
        var ship = createResourceForUser.ship(new CreateResourceForUserCommand(request.getEmail()));
        var protoShip = shipMapper.toProto(ship);
        responseObserver.onNext(AdminCreateShipResponse.newBuilder().setShip(protoShip).build());
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ROOT" })
    public void createRandomMonkeynaut(AdminCreateMonkeynautRequest request, StreamObserver<AdminCreateMonkeynautResponse> responseObserver) {
        var monkeynaut = createResourceForUser.monkeynaut(new CreateResourceForUserCommand(request.getEmail()));
        var protoMonkeynaut = monkeynautMapper.toProto(monkeynaut);
        responseObserver.onNext(AdminCreateMonkeynautResponse.newBuilder().setMonkeynaut(protoMonkeynaut).build());
        responseObserver.onCompleted();
    }
}
