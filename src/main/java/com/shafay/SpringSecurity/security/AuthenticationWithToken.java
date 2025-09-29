package com.shafay.SpringSecurity.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Collection;

public class AuthenticationWithToken extends PreAuthenticatedAuthenticationToken {

  public AuthenticationWithToken(Object aPrincipal, String jwtToken) {
    super(aPrincipal, null);
    setDetails(jwtToken);
    setAuthenticated(true);
  }

  public AuthenticationWithToken(Object aPrincipal, Collection<? extends GrantedAuthority> anAuthorities, String jwtToken) {
    super(aPrincipal, null, anAuthorities);
    setDetails(jwtToken);
  }
}