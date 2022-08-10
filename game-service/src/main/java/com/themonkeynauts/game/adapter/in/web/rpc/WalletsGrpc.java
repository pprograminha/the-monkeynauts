package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.CreateWallet;
import com.themonkeynauts.game.application.port.in.CreateWalletCommand;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.wallets.CreateWalletRequest;
import com.themonkeynauts.proto.service.wallets.CreateWalletResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

@PresentationAdapter
@RequiredArgsConstructor
public class WalletsGrpc extends com.themonkeynauts.proto.service.wallets.WalletsGrpc.WalletsImplBase {

    private final CreateWallet createWallet;
    private final InWalletMapper mapper;

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void create(CreateWalletRequest request, StreamObserver<CreateWalletResponse> responseObserver) {
        var command = new CreateWalletCommand(request.getAddress(), request.getName());
        var createdWallet = createWallet.create(command);
        responseObserver.onNext(CreateWalletResponse.newBuilder().setWallet(mapper.toProto(createdWallet)).build());
        responseObserver.onCompleted();
    }
}
