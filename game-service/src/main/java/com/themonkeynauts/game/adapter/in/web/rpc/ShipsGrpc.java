package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InShipMapper;
import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrew;
import com.themonkeynauts.game.application.port.in.AddMonkeynautToCrewCommand;
import com.themonkeynauts.game.application.port.in.CreateShip;
import com.themonkeynauts.game.application.port.in.CreateShipCommand;
import com.themonkeynauts.game.application.port.in.FetchShip;
import com.themonkeynauts.game.application.port.in.ListShip;
import com.themonkeynauts.game.application.port.in.RemoveMonkeynautFromCrew;
import com.themonkeynauts.game.application.port.in.RemoveMonkeynautFromCrewCommand;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.proto.service.ships.AddMonkeynautToCrewRequest;
import com.themonkeynauts.proto.service.ships.AddMonkeynautToCrewResponse;
import com.themonkeynauts.proto.service.ships.CreateShipRequest;
import com.themonkeynauts.proto.service.ships.CreateShipResponse;
import com.themonkeynauts.proto.service.ships.GetShipRequest;
import com.themonkeynauts.proto.service.ships.GetShipResponse;
import com.themonkeynauts.proto.service.ships.ListShipRequest;
import com.themonkeynauts.proto.service.ships.ListShipResponse;
import com.themonkeynauts.proto.service.ships.RemoveMonkeynautFromCrewRequest;
import com.themonkeynauts.proto.service.ships.RemoveMonkeynautFromCrewResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

@PresentationAdapter
@RequiredArgsConstructor
public class ShipsGrpc extends com.themonkeynauts.proto.service.ships.ShipsGrpc.ShipsImplBase {

    private final CreateShip createShip;
    private final ListShip listShip;
    private final FetchShip fetchShip;
    private final AddMonkeynautToCrew addMonkeynautToCrew;
    private final RemoveMonkeynautFromCrew removeMonkeynautFromCrew;
    private final InShipMapper mapper;

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void create(CreateShipRequest request, StreamObserver<CreateShipResponse> responseObserver) {
        var createdShip = createShip(request);
        var protoShip = mapper.toProto(createdShip);
        var response = CreateShipResponse.newBuilder().setShip(protoShip).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void list(ListShipRequest request, StreamObserver<ListShipResponse> responseObserver) {
        var ships = listShip.all();
        var protoShips = ships.stream()
                .map(ship -> mapper.toProto(ship))
                .toList();
        var response = ListShipResponse.newBuilder().addAllShips(protoShips).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void get(GetShipRequest request, StreamObserver<GetShipResponse> responseObserver) {
        var ship = fetchShip.byId(UUID.fromString(request.getShipId()));
        var protoShip = mapper.toProto(ship);
        responseObserver.onNext(GetShipResponse.newBuilder().setShip(protoShip).build());
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void addMonkeynautToCrew(AddMonkeynautToCrewRequest request, StreamObserver<AddMonkeynautToCrewResponse> responseObserver) {
        var command = new AddMonkeynautToCrewCommand(UUID.fromString(request.getShipId()), UUID.fromString(request.getMonkeynautId()));
        addMonkeynautToCrew.ofShip(command);
        var ship = fetchShip.byId(command.shipId());
        var protoShip = mapper.toProto(ship);
        responseObserver.onNext(AddMonkeynautToCrewResponse.newBuilder().setShip(protoShip).build());
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void removeMonkeynautFromCrew(RemoveMonkeynautFromCrewRequest request, StreamObserver<RemoveMonkeynautFromCrewResponse> responseObserver) {
        var command = new RemoveMonkeynautFromCrewCommand(UUID.fromString(request.getShipId()), UUID.fromString(request.getMonkeynautId()));
        removeMonkeynautFromCrew.ofShip(command);
        var ship = fetchShip.byId(command.shipId());
        var protoShip = mapper.toProto(ship);
        responseObserver.onNext(RemoveMonkeynautFromCrewResponse.newBuilder().setShip(protoShip).build());
        responseObserver.onCompleted();
    }

    private Ship createShip(CreateShipRequest request) {
        if (request.hasSpecified()) {
            var clazz = mapper.getClassTypeMapper().toDomain(request.getSpecified().getClass_());
            var rank = mapper.getRankTypeMapper().toDomain(request.getSpecified().getRank());
            var command = new CreateShipCommand(clazz, rank);
            return createShip.create(command);
        }
        return createShip.createRandom();
    }
}
