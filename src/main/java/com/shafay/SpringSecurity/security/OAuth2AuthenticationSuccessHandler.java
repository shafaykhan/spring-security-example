package com.shafay.SpringSecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shafay.SpringSecurity.common.util.JwtUtil;
import com.shafay.SpringSecurity.module.employee.Employee;
import com.shafay.SpringSecurity.module.employee.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;

  public OAuth2AuthenticationSuccessHandler(EmployeeService employeeService, JwtUtil jwtUtil) {
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
    Employee employee = employeeService.createEmployeeIfNotExists(oidcUser);
    String token = jwtUtil.generateToken(employee.getUsername());

    String jsonResponse = new ObjectMapper().writeValueAsString(new AuthenticationWithToken(employee, token));
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json");
    response.getWriter().print(jsonResponse);
  }
}
