package com.shafay.SpringSecurity.security.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

  @PostMapping("/login")
  public void login() {
    throw new RuntimeException();
  }
}