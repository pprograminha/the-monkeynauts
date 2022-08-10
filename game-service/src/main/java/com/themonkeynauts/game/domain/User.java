package com.themonkeynauts.game.domain;

import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ToString
public class User {

    private UUID id;
    private final String email;
    private final String nickname;
    private final String password;
    private final Account account;
    private final List<Role> roles;
    private final List<Equipment> equipments;

    private Wallet wallet;
    private LocalDateTime lastBountyHunting;

    public User(UUID id, String email, String nickname, String password, Account account) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.account = account;
        this.roles = new ArrayList<>();
        this.equipments = new ArrayList<>();
    }

    public User(String email, String nickname, String password, Account account) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.account = account;
        this.roles = new ArrayList<>();
        this.equipments = new ArrayList<>();
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }

    public void addEquipment(Equipment equipment) {
        this.equipments.add(equipment);
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public boolean hasWallet() {
        return this.wallet != null;
    }

    public void setLastBountyHunting(LocalDateTime lastBountyHunting) {
        this.lastBountyHunting = lastBountyHunting;
    }

    public UUID getId() {
        return this.id;
    }

    public String getEmail() {
        return this.email;
    }

    public String getNickname() {
        return this.nickname;
    }

    public LocalDateTime getLastBountyHunting() {
        return this.lastBountyHunting;
    }

    public String getPassword() {
        return this.password;
    }

    public Account getAccount() {
        return this.account;
    }

    public List<Role> getRoles() {
        return this.roles;
    }

    public List<Equipment> getEquipments() {
        return this.equipments;
    }

    public Wallet getWallet() {
        return this.wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return new EqualsBuilder().append(id, user.id).append(email, user.email).append(nickname, user.nickname).append(account, user.account).append(roles, user.roles).append(equipments, user.equipments).append(lastBountyHunting, user.lastBountyHunting).append(wallet, user.wallet).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(email).append(nickname).append(account).append(roles).append(equipments).append(lastBountyHunting).append(wallet).toHashCode();
    }
}
