package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InRoleMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.AuthenticateUserCommand;
import com.themonkeynauts.game.application.port.in.AuthenticationUser;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import com.themonkeynauts.proto.service.authentication.AuthenticatedRequest;
import com.themonkeynauts.proto.service.authentication.AuthenticatedResponse;
import com.themonkeynauts.proto.service.authentication.AuthenticationRequest;
import com.themonkeynauts.proto.service.authentication.AuthenticationResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationGrpcTest {

    private AuthenticationGrpc authenticationGrpc;

    @Mock
    private AuthenticationUser authenticationUser;

    @Mock
    private StreamObserver<AuthenticationResponse> authenticateStreamObserver;

    @Mock
    private StreamObserver<AuthenticatedResponse> authenticatedStreamObserver;

    @BeforeEach
    public void setUp() {
        authenticationGrpc = new AuthenticationGrpc(authenticationUser, new InUserMapper(new InRoleMapper(), new InWalletMapper()));
    }

    @Test
    public void shouldAuthenticate() {
        var command = new AuthenticateUserCommand("email", "password");
        when(authenticationUser.login(command)).thenReturn("token");

        var request = AuthenticationRequest.newBuilder().setEmail("email").setPassword("password").build();
        authenticationGrpc.authenticate(request, authenticateStreamObserver);

        verify(authenticateStreamObserver).onNext(AuthenticationResponse.newBuilder().setToken("token").build());
        verify(authenticateStreamObserver).onCompleted();
    }

    @Test
    public void shouldReturnAuthenticated() {
        var id = UUID.randomUUID();
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var user = new User(id, "email", "nickname", "password", account);
        user.addRole(Role.ADMIN);
        user.addRole(Role.PLAYER);
        when(authenticationUser.authenticated()).thenReturn(user);

        authenticationGrpc.authenticated(AuthenticatedRequest.newBuilder().build(), authenticatedStreamObserver);

        var proto = com.themonkeynauts.proto.common.messages.User.newBuilder()
                .setId(id.toString())
                .setEmail("email")
                .setNickname("nickname")
                .addAllRoles(asList(com.themonkeynauts.proto.common.messages.Role.ADMIN, com.themonkeynauts.proto.common.messages.Role.PLAYER))
                .build();
        verify(authenticatedStreamObserver).onNext(AuthenticatedResponse.newBuilder().setUser(proto).build());
        verify(authenticatedStreamObserver).onCompleted();
    }

}