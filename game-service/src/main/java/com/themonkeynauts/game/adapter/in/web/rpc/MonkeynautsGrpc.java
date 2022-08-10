package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InMonkeynautMapper;
import com.themonkeynauts.game.application.port.in.CreateMonkeynaut;
import com.themonkeynauts.game.application.port.in.CreateMonkeynautCommand;
import com.themonkeynauts.game.application.port.in.ListMonkeynaut;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.proto.service.monkeynauts.CreateMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.CreateMonkeynautResponse;
import com.themonkeynauts.proto.service.monkeynauts.ListMonkeynautRequest;
import com.themonkeynauts.proto.service.monkeynauts.ListMonkeynautResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

@PresentationAdapter
@RequiredArgsConstructor
public class MonkeynautsGrpc extends com.themonkeynauts.proto.service.monkeynauts.MonkeynautsGrpc.MonkeynautsImplBase {

    private final CreateMonkeynaut createMonkeynaut;
    private final ListMonkeynaut listMonkeynaut;
    private final InMonkeynautMapper mapper;

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void create(CreateMonkeynautRequest request, StreamObserver<CreateMonkeynautResponse> responseObserver) {
        var createdMonkeynaut = createMonkeynaut(request);
        var protoMonkeynaut = mapper.toProto(createdMonkeynaut);
        var response = CreateMonkeynautResponse.newBuilder().setMonkeynaut(protoMonkeynaut).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void list(ListMonkeynautRequest request, StreamObserver<ListMonkeynautResponse> responseObserver) {
        var monkeynauts = listMonkeynaut.all();
        var protoMonkeynauts = monkeynauts.stream()
                .map(monkeynaut -> mapper.toProto(monkeynaut))
                .toList();
        var response = ListMonkeynautResponse.newBuilder().addAllMonkeynauts(protoMonkeynauts).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Monkeynaut createMonkeynaut(CreateMonkeynautRequest request) {
        if (request.hasSpecified()) {
            var clazz = mapper.getClassTypeMapper().toDomain(request.getSpecified().getClass_());
            var rank = mapper.getRankTypeMapper().toDomain(request.getSpecified().getRank());
            var command = new CreateMonkeynautCommand(clazz, rank);
            return createMonkeynaut.create(command);
        }
        return createMonkeynaut.createRandom();
    }
}
