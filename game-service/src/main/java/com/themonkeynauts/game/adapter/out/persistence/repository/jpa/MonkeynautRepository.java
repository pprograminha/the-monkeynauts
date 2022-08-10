package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MonkeynautRepository extends JpaRepository<MonkeynautEntity, UUID> {

    @Query("""
    SELECT monkeynaut
    FROM MonkeynautEntity monkeynaut
    INNER JOIN FETCH monkeynaut.user
    INNER JOIN FETCH monkeynaut.operator
    WHERE monkeynaut.id = :id     
    """)
    Optional<MonkeynautEntity> findById(@Param("id") UUID id);

    List<MonkeynautEntity> findAllByOrderByNumberAsc();
}
