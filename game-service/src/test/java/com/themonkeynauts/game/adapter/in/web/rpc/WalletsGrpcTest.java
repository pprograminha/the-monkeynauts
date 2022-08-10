package com.themonkeynauts.game.adapter.in.web.rpc;

import com.themonkeynauts.game.adapter.in.web.mapper.InWalletMapper;
import com.themonkeynauts.game.application.port.in.CreateWallet;
import com.themonkeynauts.game.application.port.in.CreateWalletCommand;
import com.themonkeynauts.proto.common.messages.Wallet;
import com.themonkeynauts.proto.service.wallets.CreateWalletRequest;
import com.themonkeynauts.proto.service.wallets.CreateWalletResponse;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletsGrpcTest {

    private WalletsGrpc walletsGrpc;

    @Mock
    private CreateWallet createWallet;

    @Mock
    private StreamObserver<CreateWalletResponse> responseStreamObserver;

    @BeforeEach
    public void setUp() {
        walletsGrpc = new WalletsGrpc(createWallet, new InWalletMapper());
    }

    @Test
    public void shouldCreateWallet() {
        var walletId = UUID.randomUUID();
        var walletAddress = UUID.randomUUID().toString();
        var command = new CreateWalletCommand(walletAddress, "BNB");
        var wallet = new com.themonkeynauts.game.domain.Wallet(walletId, walletAddress, "BNB");
        when(createWallet.create(command)).thenReturn(wallet);

        var request = CreateWalletRequest.newBuilder().setAddress(walletAddress).setName("BNB").build();
        walletsGrpc.create(request, responseStreamObserver);

        var protoWallet = Wallet.newBuilder().setId(walletId.toString()).setAddress(walletAddress).setName("BNB").setBalance(0.0f).build();
        verify(responseStreamObserver).onNext(CreateWalletResponse.newBuilder().setWallet(protoWallet).build());
        verify(responseStreamObserver).onCompleted();
    }

}