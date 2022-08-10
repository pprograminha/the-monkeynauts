package com.themonkeynauts.game.adapter.out.persistence.repository.jpa;

import com.themonkeynauts.game.adapter.out.persistence.entity.MonkeynautNumberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MonkeynautNumberRepository extends JpaRepository<MonkeynautNumberEntity, Long> {

    @Query("""
    SELECT COALESCE(MAX(monkeynaut.number), 0) FROM MonkeynautNumberEntity monkeynaut
    """)
    Long getLastNumber();

    @Modifying
    @Query("""
    UPDATE MonkeynautNumberEntity monkeynaut SET monkeynaut.number = monkeynaut.number + 1
    """)
    void incrementNumber();
}
