package com.shafay.SpringSecurity.security.provider;

import com.shafay.SpringSecurity.common.util.JwtUtil;
import com.shafay.SpringSecurity.module.employee.Employee;
import com.shafay.SpringSecurity.module.employee.EmployeeService;
import com.shafay.SpringSecurity.security.AuthenticationWithToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;
  private final PasswordEncoder passwordEncoder;

  public LoginAuthenticationProvider(EmployeeService employeeService, JwtUtil jwtUtil, @Lazy PasswordEncoder passwordEncoder) {
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String username = authentication.getName();
    String password = (String) authentication.getCredentials();

    Employee employee = employeeService.findByUsername(username);

    if (!passwordEncoder.matches(password, employee.getPassword()))
      throw new BadCredentialsException("Invalid password!");

    String token = jwtUtil.generateToken(employee.getUsername());
    return new AuthenticationWithToken(employee, token);
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
  }
}