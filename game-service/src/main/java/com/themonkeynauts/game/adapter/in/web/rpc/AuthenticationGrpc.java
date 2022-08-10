package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.application.port.in.AuthenticateUserCommand;
import com.themonkeynauts.game.application.port.in.AuthenticationUser;
import com.themonkeynauts.game.common.annotation.PresentationAdapter;
import com.themonkeynauts.proto.service.authentication.AuthenticatedRequest;
import com.themonkeynauts.proto.service.authentication.AuthenticatedResponse;
import com.themonkeynauts.proto.service.authentication.AuthenticationRequest;
import com.themonkeynauts.proto.service.authentication.AuthenticationResponse;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;

@PresentationAdapter
@RequiredArgsConstructor
public class AuthenticationGrpc extends com.themonkeynauts.proto.service.authentication.AuthenticationGrpc.AuthenticationImplBase {

    private final AuthenticationUser authenticationUser;
    private final InUserMapper mapper;

    @Override
    public void authenticate(AuthenticationRequest request, StreamObserver<AuthenticationResponse> responseObserver) {
        var command = new AuthenticateUserCommand(request.getEmail(), request.getPassword());
        var token = authenticationUser.login(command);
        responseObserver.onNext(AuthenticationResponse.newBuilder().setToken(token).build());
        responseObserver.onCompleted();
    }

    @Override
    @Secured({ "ROLE_ADMIN", "ROLE_PLAYER" })
    public void authenticated(AuthenticatedRequest request, StreamObserver<AuthenticatedResponse> responseObserver) {
        var user = authenticationUser.authenticated();
        responseObserver.onNext(AuthenticatedResponse.newBuilder().setUser(mapper.toProto(user)).build());
        responseObserver.onCompleted();
    }
}
