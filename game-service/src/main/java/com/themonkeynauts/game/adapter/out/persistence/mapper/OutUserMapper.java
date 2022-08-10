package com.themonkeynauts.game.adapter.out.persistence.mapper;

import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEntity;
import com.themonkeynauts.game.adapter.out.persistence.entity.RoleEnum;
import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import com.themonkeynauts.game.common.annotation.MapperAdapter;
import com.themonkeynauts.game.domain.Role;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;

@MapperAdapter
@RequiredArgsConstructor
public class OutUserMapper {

    private final OutAccountMapper accountMapper;
    private final OutEquipmentMapper equipmentMapper;
    private final OutWalletMapper walletMapper;

    public UserEntity toEntity(User domain, String password) {
        var roles = domain.getRoles().stream()
                .map(role -> RoleEntity.builder().roleId(RoleEnum.valueOf(role.name())).build())
                .collect(Collectors.toSet());
        var equipments = domain.getEquipments().stream()
                .map(equipment -> equipmentMapper.toEntity(equipment))
                .collect(Collectors.toSet());
        var account = accountMapper.toEntity(domain.getAccount());
        var wallet = domain.getWallet() != null ? walletMapper.toEntity(domain.getWallet()) : null;
        var entity = UserEntity.builder()
                .id(domain.getId())
                .wallet(wallet)
                .email(domain.getEmail().toLowerCase())
                .nickname(domain.getNickname().toLowerCase())
                .password(password)
                .roles(roles)
                .account(account)
                .equipments(equipments)
                .lastBountyHunting(domain.getLastBountyHunting())
                .build();
        return entity;
    }

    public UserEntity toEntity(User domain) {
        return this.toEntity(domain, domain.getPassword());
    }

    public User toDomain(UserEntity entity) {
        var account = accountMapper.toDomain(entity.getTenant());
        var user = new User(entity.getId(), entity.getEmail(), entity.getNickname(), entity.getPassword(), account);
        user.setLastBountyHunting(entity.getLastBountyHunting());
        var roles = entity.getRoles().stream()
                .map(role -> role.getRoleId().name())
                .map(name -> Role.valueOf(name))
                .toList();
        roles.forEach(role -> user.addRole(role));
        var equipments = entity.getEquipments().stream()
                .map(equipmentMapper::toDomain)
                .toList();
        equipments.forEach(equipment -> user.addEquipment(equipment));
        if (entity.getWallet() != null) {
            user.setWallet(walletMapper.toDomain(entity.getWallet()));
        }
        return user;
    }

    public OutEquipmentMapper getEquipmentMapper() {
        return this.equipmentMapper;
    }

}
