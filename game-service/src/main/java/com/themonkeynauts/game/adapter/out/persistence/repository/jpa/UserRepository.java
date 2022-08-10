package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByEmailAndPassword(String email, String password);

    Optional<UserEntity> findByNickname(String nickname);

    @Modifying
    @Query("""
    UPDATE UserEntity user
    SET user.lastBountyHunting = :time
    WHERE user.id = :userId
    """)
    void startBountyHunting(@Param("userId") UUID userId, @Param("time")LocalDateTime time);
}
