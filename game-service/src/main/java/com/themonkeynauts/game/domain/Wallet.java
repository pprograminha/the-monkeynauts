package com.themonkeynauts.game.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.util.UUID;

public class Wallet {

    private UUID id;

    private final String address;
    private final String name;

    private Balance balance;

    public Wallet(String address, String name) {
        this.address = address;
        this.name = name;
        this.balance = new Balance();
    }

    public Wallet(UUID id, String address, String name) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.balance = new Balance();
    }

    public void add(BigDecimal value) {
        this.balance = new Balance(balance.add(value));
    }

    public UUID getId() {
        return this.id;
    }

    public String getAddress() {
        return this.address;
    }

    public String getName() {
        return this.name;
    }

    public Balance getBalance() {
        return this.balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Wallet wallet = (Wallet) o;

        return new EqualsBuilder().append(id, wallet.id).append(name, wallet.name).append(balance, wallet.balance).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(balance).toHashCode();
    }
}
