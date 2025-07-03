package com.sec.movietalk.userinfo.security;

import com.sec.movietalk.common.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CurrentUserDetails implements UserDetails {

    private final Long userId;
    private final String email;
    private final String password;
    private final String nickname;
    private final String role;

    public CurrentUserDetails(User user) {

        this.userId = user.getUserId();     // PK
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.role));
    }

    @Override
    public String getUsername() {
        return email;
    }
    @Override
    public String getPassword() {
        return password;
    }

    // 아래는 전부 true로 하면 됨
    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }

}