package com.sec.movietalk.review.repository;

import com.sec.movietalk.common.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByNickname(String nickname);
}

