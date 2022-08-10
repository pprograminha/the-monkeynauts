package com.themonkeynauts.game.application.port.out.persistence;

public interface LoadWalletPort {

    boolean existsByAddress(String address);
}
