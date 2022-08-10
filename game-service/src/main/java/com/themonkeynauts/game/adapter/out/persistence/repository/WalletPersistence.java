package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.mapper.OutWalletMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.WalletRepository;
import com.themonkeynauts.game.adapter.out.security.SecurityUtils;
import com.themonkeynauts.game.application.port.out.persistence.LoadWalletPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveWalletPort;
import com.themonkeynauts.game.common.annotation.PersistenceAdapter;
import com.themonkeynauts.game.domain.Wallet;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@PersistenceAdapter
@RequiredArgsConstructor
public class WalletPersistence implements LoadWalletPort, SaveWalletPort {

    private final WalletRepository repository;
    private final UserRepository userRepository;
    private final OutWalletMapper mapper;
    private final OutUserMapper userMapper;
    private final SecurityUtils securityUtils;

    @Override
    public boolean existsByAddress(String address) {
        return repository.existsByAddress(address);
    }

    @Override
    @Transactional
    public Wallet save(Wallet wallet) {
        var entity = mapper.toEntity(wallet);

        var authenticatedUser = securityUtils.getAuthenticatedUser();
        var userEntity = userRepository.findById(authenticatedUser.getId()).get();
        entity.setUser(userEntity);

        var savedEntity = repository.save(entity);

        userEntity.setWallet(savedEntity);
        userRepository.save(userEntity);

        return mapper.toDomain(savedEntity);
    }
}
