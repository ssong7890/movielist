package com.sec.movietalk.userinfo.service;

import com.sec.movietalk.common.domain.user.User;
import com.sec.movietalk.userinfo.repository.UserRepository;
import com.sec.movietalk.userinfo.security.CurrentUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("유저를 찾을 수 없습니다.");
        }

        return new CurrentUserDetails(user);
    }
}
