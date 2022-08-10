package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.adapter.out.persistence.entity.ShipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ShipRepository extends JpaRepository<ShipEntity, UUID> {

    @Query("""
        SELECT ship FROM ShipEntity ship 
        LEFT JOIN FETCH ship.monkeynauts 
        WHERE ship.id = :id
    """)
    Optional<ShipEntity> findByIdFetchMonkeynauts(@Param("id") UUID id);
}
