package com.shafay.SpringSecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(requests -> requests
        .requestMatchers("/", "index", "/css/*", "/js/*").permitAll() // disable security urls
        .anyRequest().authenticated())
        .oauth2Login(Customizer.withDefaults())
        .logout(logout -> logout
            .logoutSuccessUrl("/")   // redirect here after logout
            .invalidateHttpSession(true)  // clear HttpSession
            .clearAuthentication(true)    // clear SecurityContextHolder
            .deleteCookies("JSESSIONID")  // remove session cookie
        );
    return http.build();
  }
}