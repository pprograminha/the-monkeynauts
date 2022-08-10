package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InEquipmentMapper;
import com.themonkeynauts.game.application.port.in.EquipmentToUser;
import com.themonkeynauts.game.application.port.in.EquipmentToUserCommand;
import com.themonkeynauts.game.application.port.in.ListEquipmentsFromUser;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.equipments.GrantToUserEquipmentRequest;
import com.themonkeynauts.proto.service.equipments.GrantToUserEquipmentResponse;
import com.themonkeynauts.proto.service.equipments.ListFromUserEquipmentRequest;
import com.themonkeynauts.proto.service.equipments.ListFromUserEquipmentResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

import java.util.UUID;

@PresentationAdapter
@RequiredArgsConstructor
public class EquipmentsGrpc extends com.themonkeynauts.proto.service.equipments.EquipmentsGrpc.EquipmentsImplBase {

    private final EquipmentToUser equipmentToUser;
    private final ListEquipmentsFromUser listEquipmentsFromUser;
    private final InEquipmentMapper equipmentMapper;

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void grantToUser(GrantToUserEquipmentRequest request, StreamObserver<GrantToUserEquipmentResponse> responseObserver) {
        var command = new EquipmentToUserCommand(UUID.fromString(request.getEquipmentId()));
        equipmentToUser.grant(command);
        responseObserver.onNext(GrantToUserEquipmentResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void listFromUser(ListFromUserEquipmentRequest request, StreamObserver<ListFromUserEquipmentResponse> responseObserver) {
        var equipments = listEquipmentsFromUser.all();
        var protos = equipments.stream().map(equipmentMapper::toProto).toList();
        var response = ListFromUserEquipmentResponse.newBuilder().addAllEquipments(protos).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
