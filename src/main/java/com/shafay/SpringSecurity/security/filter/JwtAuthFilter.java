package com.shafay.SpringSecurity.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shafay.SpringSecurity.common.util.JwtUtil;
import com.shafay.SpringSecurity.module.employee.EmployeeService;
import com.shafay.SpringSecurity.security.auth.AuthResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;

  public JwtAuthFilter(EmployeeService employeeService, JwtUtil jwtUtil) {
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String header = request.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    String token = header.substring(7);
    String username = jwtUtil.extractUsername(token);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      var employeeDetails = employeeService.findByUsername(username);

      if (!jwtUtil.validateToken(token, employeeDetails.getUsername())) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        AuthResponse authResponse = new AuthResponse(null, "Invalid or expired token");
        new ObjectMapper().writeValue(response.getWriter(), authResponse);
        return;
      }

      var authToken = new PreAuthenticatedAuthenticationToken(employeeDetails, null, Collections.emptyList());
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    filterChain.doFilter(request, response);
  }
}