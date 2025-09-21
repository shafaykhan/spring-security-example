package com.shafay.SpringSecurity.module.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @GetMapping
  public ResponseEntity<Map<String, Object>> authInfo(@AuthenticationPrincipal OAuth2User principal) {
    if (principal == null)
      return ResponseEntity.ok(Map.of("error", "Not authenticated"));

    return ResponseEntity.ok(Map.of("name", Objects.requireNonNull(principal.getAttribute("name")),
        "email", Objects.requireNonNull(principal.getAttribute("email"))));
  }
}
