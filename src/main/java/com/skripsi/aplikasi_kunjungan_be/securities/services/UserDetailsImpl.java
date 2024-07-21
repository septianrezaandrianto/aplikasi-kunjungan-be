package com.skripsi.aplikasi_kunjungan_be.securities.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.skripsi.aplikasi_kunjungan_be.entities.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;

    private String username;
    @JsonIgnore
    private String password;

    private String role;

    public UserDetailsImpl(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public static UserDetailsImpl build(Admin admin) {
        return new UserDetailsImpl(
                admin.getId(),
                admin.getUsername(),
                admin.getPassword(),
                admin.getRole());
    }

    public String getId() {
        return id;
    }
    public String getRole() { return role; }
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
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

}
