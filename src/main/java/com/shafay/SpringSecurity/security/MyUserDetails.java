package com.shafay.SpringSecurity.security;

import com.shafay.SpringSecurity.common.enums.Status;
import com.shafay.SpringSecurity.module.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class MyUserDetails implements UserDetails {

  private String userName;
  private String password;
  private List<GrantedAuthority> authorities;
  private Boolean isActive;

  public MyUserDetails() {
  }

  public MyUserDetails(User user) {
    this.userName = user.getName();
    this.password = user.getPassword();
    this.authorities = Arrays.stream(user.getRole().split(","))
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());
    this.isActive = user.getStatus().equals(Status.ACTIVE);
  }

  @Override
  public String getUsername() {
    return userName;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
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
}
