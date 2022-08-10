package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.ShipEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutShipMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.ShipRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.out.persistence.LoadShipPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveShipPort;
import com.themonkeynauts.game.common.annotation.PersistenceAdapter;
import com.themonkeynauts.game.domain.Ship;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class ShipPersistence implements SaveShipPort, LoadShipPort {

    private final ShipRepository repository;
    private final OutUserMapper userMapper;
    private final OutShipMapper mapper;
    private final SecurityUtils securityUtils;

    @Override
    public Ship save(Ship ship) {
        var entity = mapper.toEntity(ship);

        if (ship.getOwner() == null && ship.getOperator() == null) {
            var authenticatedUser = securityUtils.getAuthenticatedUser();
            setAuthenticatedUser(authenticatedUser, entity);
        }

        var savedEntity = repository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    private void setAuthenticatedUser(User authenticatedUser, ShipEntity entity) {
        var userEntity = userMapper.toEntity(authenticatedUser);
        entity.setUser(userEntity);
        entity.setOperator(userEntity);
    }

    @Override
    public List<Ship> all() {
        var entities = repository.findAll();
        return entities.stream().map(entity -> mapper.toDomain(entity)).toList();
    }

    @Override
    public Optional<Ship> byId(UUID id) {
        var entity = repository.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity.get()));
    }
}
