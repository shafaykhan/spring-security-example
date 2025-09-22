package com.shafay.SpringSecurity.security.auth;

import com.shafay.SpringSecurity.common.util.JwtUtil;
import com.shafay.SpringSecurity.module.employee.Employee;
import com.shafay.SpringSecurity.module.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticationManager authenticationManager;
  private final EmployeeService employeeService;
  private final JwtUtil jwtUtil;

  @Value("${app.oauth2.google.client_id}")
  private String googleClientId;

  public AuthController(AuthenticationManager authenticationManager, EmployeeService employeeService, JwtUtil jwtUtil) {
    this.authenticationManager = authenticationManager;
    this.employeeService = employeeService;
    this.jwtUtil = jwtUtil;
  }

  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody LoginRequest request) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
      return ResponseEntity.ok(jwtUtil.generateToken(request.getUsername()));
    } catch (AuthenticationException e) {
      throw new RuntimeException("Invalid credentials!");
    }
  }

  @GetMapping("/google/verify-token")
  public ResponseEntity<String> verifyGoogleIdToken(@RequestHeader String idToken) {
    NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder
        .withJwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
        .build();

    Jwt jwt = jwtDecoder.decode(idToken);

    if (!jwt.getAudience().contains(googleClientId)) {
      throw new RuntimeException("Invalid audience");
    }

    if (!"https://accounts.google.com".equals(jwt.getIssuer().toString())) {
      throw new RuntimeException("Invalid issuer");
    }

    Employee employee = employeeService.createEmployeeIfNotExists(jwt);
    return ResponseEntity.ok(jwtUtil.generateToken(employee.getUsername()));
  }
}