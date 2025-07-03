package com.sec.movietalk.actor.repository;

import com.sec.movietalk.actor.dto.ActorCache;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActorCacheRepository extends JpaRepository<ActorCache, Long> {
    boolean existsById(Long id);
}
