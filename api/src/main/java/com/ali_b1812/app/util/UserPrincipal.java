package com.ali_b1812.app.util;

import com.ali_b1812.app.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@AllArgsConstructor
@Builder
public class UserPrincipal implements UserDetails {
    
    private final Long id;
    private final String username;
    private final String email;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean isActive;
    
    public static UserPrincipal create(User user) {
        return UserPrincipal.builder()
            .id(user.getId())
            .username(user.getUserName())
            .email(user.getEmail())
            .password(user.getHashedPassword())
            .authorities(Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
            ))
            .isActive(user.getIsActive())
            .build();
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    
    @Override
    public boolean isEnabled() {
        return isActive;
    }
}