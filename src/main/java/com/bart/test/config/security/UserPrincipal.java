package com.bart.test.config.security;

import com.bart.test.entity.Role;
import com.bart.test.entity.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Data
public class UserPrincipal implements UserDetails {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    private Role role;
    private boolean deleted;

    public UserPrincipal(Long id, String name, String email, String password, boolean deleted, List<GrantedAuthority> authorities, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.deleted = deleted;
        this.authorities = authorities;
        this.role = role;
    }


    public static UserDetails create(User user) {
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ user.getRole().name()));
        return new UserPrincipal(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.isDeleted(),
                authorities,
                user.getRole()
        );
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
        return email;
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
        return !deleted;
    }
}
