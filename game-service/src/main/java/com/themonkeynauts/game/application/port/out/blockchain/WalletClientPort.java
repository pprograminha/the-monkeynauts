package com.themonkeynauts.game.application.port.out.blockchain;

public interface WalletClientPort {

    boolean isValidAddress(String address);
}
