package com.themonkeynauts.game.adapter.out.persistence.repository;

import com.themonkeynauts.game.adapter.out.persistence.mapper.OutUserMapper;
import com.themonkeynauts.game.adapter.out.persistence.repository.jpa.UserRepository;
import com.themonkeynauts.game.application.port.out.persistence.LoadUserPort;
import com.themonkeynauts.game.application.port.out.persistence.SaveUserPort;
import com.themonkeynauts.game.common.annotation.PersistenceAdapter;
import com.themonkeynauts.game.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class UserPersistence implements LoadUserPort, SaveUserPort {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final OutUserMapper mapper;

    @Override
    public Optional<User> byEmailAndPassword(String email, String password) {
        var lowerCaseEmail = email.toLowerCase();
        var entity = userRepository.findByEmail(lowerCaseEmail);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        if (!encoder.matches(password, entity.get().getPassword())) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    public Optional<User> byEmail(String email) {
        var lowerCaseEmail = email.toLowerCase();
        var entity = userRepository.findByEmail(lowerCaseEmail);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    public Optional<User> byNickname(String nickname) {
        var lowerCaseNickname = nickname.toLowerCase();
        var entity = userRepository.findByNickname(lowerCaseNickname);
        if (entity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(mapper.toDomain(entity.get()));
    }

    @Override
    @Transactional
    public User save(User player) {
        var entity = mapper.toEntity(player, encoder.encode(player.getPassword()));

        var savedEntity = userRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void startBountyHuntingQuest(UUID userId, LocalDateTime time) {
        userRepository.startBountyHunting(userId, time);
    }
}
