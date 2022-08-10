package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.EquipmentRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.out.persistence.GrantEquipmentPort;
import com.themonkeynauts.game.application.port.out.persistence.LoadEquipmentPort;
import com.themonkeynauts.game.common.annotation.PersistenceAdapter;
import com.themonkeynauts.game.domain.Equipment;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class EquipmentPersistence implements LoadEquipmentPort, GrantEquipmentPort {

    private final SecurityUtils securityUtils;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final OutUserMapper userMapper;

    @Override
    public Optional<Equipment> byId(UUID id) {
        var entity = equipmentRepository.findById(id);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(userMapper.getEquipmentMapper().toDomain(entity.get()));
    }

    @Override
    public List<Equipment> all() {
        var authenticatedUser = securityUtils.getAuthenticatedUser();
        return authenticatedUser.getEquipments();
    }

    @Override
    @Transactional
    public void grant(Equipment equipment) {
        var authenticatedUser = securityUtils.getAuthenticatedUser();

        authenticatedUser.addEquipment(equipment);

        var userEntity = userMapper.toEntity(authenticatedUser);
        userRepository.save(userEntity);
    }
}
