package com.sec.movietalk.actor.service;

import com.sec.movietalk.actor.dto.ActorCache;
import com.sec.movietalk.actor.repository.ActorCacheRepository;
import org.springframework.stereotype.Service;

@Service
public class ActorCacheService {
    private final ActorCacheRepository repository;

    public ActorCacheService(ActorCacheRepository repository) {
        this.repository = repository;
    }

    public void saveIfNotExists(Long id, String name, Integer gender, String profilePath) {
        if (!repository.existsById(id)) {
            ActorCache actor = new ActorCache(id, name, gender, profilePath);
            repository.save(actor);
        }
    }
}
