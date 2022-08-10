package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InRoleMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InUserMapper;
import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.AuthenticateUserCommand;
import com.themonkeynauts.game.application.port.in.AuthenticationUser;
import com.themonkeynauts.game.application.port.in.CreatePlayer;
import com.themonkeynauts.game.application.port.in.CreatePlayerCommand;
import com.themonkeynauts.game.domain.Account;
import com.themonkeynauts.proto.common.messages.Role;
import com.themonkeynauts.proto.common.messages.User;
import com.themonkeynauts.proto.service.players.CreatePlayerRequest;
import com.themonkeynauts.proto.service.players.CreatePlayerResponse;
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
class PlayersGrpcTest {

    private PlayersGrpc playersGrpc;

    @Mock
    private CreatePlayer createPlayer;

    @Mock
    private AuthenticationUser authentication;

    @Mock
    private StreamObserver<CreatePlayerResponse> responseStreamObserver;

    @BeforeEach
    public void setUp() {
        playersGrpc = new PlayersGrpc(createPlayer, authentication, new InUserMapper(new InRoleMapper(), new InWalletMapper()));
    }

    @Test
    public void shouldCreatePlayer() {
        var id = UUID.randomUUID();
        var command = new CreatePlayerCommand("EMAIL", "PASSWORD", "NICKNAME");
        var accountId = UUID.randomUUID();
        var account = new Account(accountId);
        var createdPlayer = new com.themonkeynauts.game.domain.User(id, "EMAIL", "NICKNAME", "PASSWORD", account);
        createdPlayer.addRole(com.themonkeynauts.game.domain.Role.ADMIN);
        createdPlayer.addRole(com.themonkeynauts.game.domain.Role.PLAYER);

        when(createPlayer.create(command)).thenReturn(createdPlayer);
        when(authentication.login(new AuthenticateUserCommand("EMAIL", "PASSWORD"))).thenReturn("token");

        var request = CreatePlayerRequest.newBuilder().setEmail("EMAIL").setPassword("PASSWORD").setNickname("NICKNAME").build();
        playersGrpc.create(request, responseStreamObserver);

        var proto = User.newBuilder().setId(id.toString()).setEmail("EMAIL").setNickname("NICKNAME").addAllRoles(asList(Role.ADMIN, Role.PLAYER)).build();
        verify(responseStreamObserver).onNext(CreatePlayerResponse.newBuilder().setPlayer(proto).setToken("token").build());
        verify(responseStreamObserver).onCompleted();
    }

}