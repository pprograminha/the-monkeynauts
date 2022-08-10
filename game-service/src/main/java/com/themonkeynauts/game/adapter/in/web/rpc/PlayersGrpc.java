package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.application.port.in.AuthenticateUserCommand;
import com.themonkeynauts.game.application.port.in.AuthenticationUser;
import com.themonkeynauts.game.application.port.in.CreatePlayer;
import com.themonkeynauts.game.application.port.in.CreatePlayerCommand;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;

@PresentationAdapter
@RequiredArgsConstructor
public class PlayersGrpc extends com.themonkeynauts.proto.service.players.PlayersGrpc.PlayersImplBase {

    private final CreatePlayer createPlayer;
    private final AuthenticationUser authentication;
    private final InUserMapper mapper;

    @Override
    public void create(CreatePlayerRequest request, StreamObserver<CreatePlayerResponse> responseObserver) {
        var command = new CreatePlayerCommand(request.getEmail(), request.getPassword(), request.getNickname());
        var createdPlayer = createPlayer.create(command);
        var token = authentication.login(new AuthenticateUserCommand(request.getEmail(), request.getPassword()));
        var response = CreatePlayerResponse.newBuilder().setPlayer(mapper.toProto(createdPlayer)).setToken(token).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
