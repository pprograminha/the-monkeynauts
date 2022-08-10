package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutMonkeynautMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.MonkeynautNumberRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.MonkeynautRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.out.persistence.LoadMonkeynautPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveMonkeynautPort;
import com.themonkeynauts.game.common.annotation.PersistenceAdapter;
import com.themonkeynauts.game.domain.Monkeynaut;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class MonkeynautPersistence implements SaveMonkeynautPort, LoadMonkeynautPort {

    private final OutMonkeynautMapper mapper;
    private final SecurityUtils securityUtils;
    private final MonkeynautRepository monkeynautRepository;
    private final MonkeynautNumberRepository monkeynautNumberRepository;

    @Override
    @Transactional
    public Monkeynaut save(Monkeynaut monkeynaut) {
        var entity = mapper.toEntity(monkeynaut);

        updateMonkeynautNumber(entity);

        if (monkeynaut.getPlayer() == null && monkeynaut.getOperator() == null) {
            var authenticatedUser = securityUtils.getAuthenticatedUser();
            setAuthenticatedUser(authenticatedUser, entity);
        }

        var savedEntity = monkeynautRepository.save(entity);

        return mapper.toDomain(savedEntity);
    }

    private void updateMonkeynautNumber(MonkeynautEntity entity) {
        if (entity.getNumber() == null) {
            var lastNumber = monkeynautNumberRepository.getLastNumber();
            entity.setNumber(lastNumber + 1);
            monkeynautNumberRepository.incrementNumber();
        }
    }

    private void setAuthenticatedUser(User authenticatedUser, MonkeynautEntity entity) {
        var userEntity = mapper.getUserMapper().toEntity(authenticatedUser);
        entity.setUser(userEntity);
        entity.setOperator(userEntity);
    }

    @Override
    public List<Monkeynaut> all() {
        var entities = monkeynautRepository.findAllByOrderByNumberAsc();
        return entities.stream().map(entity -> mapper.toDomain(entity)).toList();
    }

    @Override
    public Optional<Monkeynaut> byId(UUID id) {
        var entity = monkeynautRepository.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity.get()));
    }
}
