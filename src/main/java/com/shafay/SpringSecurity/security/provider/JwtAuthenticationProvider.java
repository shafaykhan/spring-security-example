package com.shafay.SpringSecurity.security.provider;

import com.shafay.SpringSecurity.common.util.JwtUtil;
import com.shafay.SpringSecurity.module.employee.Employee;
import com.shafay.SpringSecurity.module.employee.EmployeeService;
import com.shafay.SpringSecurity.security.AuthenticationWithToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;

  public JwtAuthenticationProvider(EmployeeService employeeService, JwtUtil jwtUtil) {
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String token = (String) authentication.getPrincipal();

    if (token == null || token.isBlank())
      throw new BadCredentialsException("Invalid token!");

    if (jwtUtil.isTokenExpired(token))
      throw new BadCredentialsException("Invalid or expired JWT token!");

    String username = jwtUtil.extractUsername(token);

    Employee employee = employeeService.findByUsername(username);

    return new AuthenticationWithToken(employee, token);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return PreAuthenticatedAuthenticationToken.class.isAssignableFrom(authentication);
  }
}