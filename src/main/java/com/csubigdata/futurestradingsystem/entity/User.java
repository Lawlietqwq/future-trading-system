package com.csubigdata.futurestradingsystem.entity;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class User implements UserDetails {

    private Long uid;

    private String username;

    private String password;

    private String email;

    private String xinyiAccount;

    private String xinyiPwd;

    private String tradingAccount;

    private String tradingPwd;

    private String company;

    private String getPassword;

    private String getUsername;

    private boolean isAccountNonExpired = true;

    private boolean isAccountNonLocked = true;

    private boolean isCredentialsNonExpired = true;

    private boolean isEnabled = true;

    Collection<? extends GrantedAuthority> getAuthorities;

    private List<Model> models;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getAuthorities;
    }

    @Override
    public String getUsername() {
        return this.username;
    }
}