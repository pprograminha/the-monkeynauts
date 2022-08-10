package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.mapper.OutAccountMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.AccountRepository;
import com.themonkeynauts.game.application.port.out.persistence.SaveAccountPort;
import com.themonkeynauts.game.common.annotation.PersistenceAdapter;
import com.themonkeynauts.game.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@PersistenceAdapter
public class AccountPersistence implements SaveAccountPort {

    private final OutAccountMapper mapper;
    private final AccountRepository repository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Account save(Account account) {
        var entity = mapper.toEntity(account);
        var savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
}
