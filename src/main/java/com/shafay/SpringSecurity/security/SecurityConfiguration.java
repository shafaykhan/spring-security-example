package com.shafay.SpringSecurity.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shafay.SpringSecurity.security.filter.AuthenticationFilter;
import com.shafay.SpringSecurity.security.provider.JwtAuthenticationProvider;
import com.shafay.SpringSecurity.security.provider.LoginAuthenticationProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  private final LoginAuthenticationProvider loginAuthenticationProvider;
  private final JwtAuthenticationProvider jwtAuthenticationProvider;
  private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  private final AuthenticationFilter authenticationFilter;

  public SecurityConfiguration(LoginAuthenticationProvider loginAuthenticationProvider, JwtAuthenticationProvider jwtAuthenticationProvider, AuthenticationFilter authenticationFilter, OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler) {
    this.loginAuthenticationProvider = loginAuthenticationProvider;
    this.jwtAuthenticationProvider = jwtAuthenticationProvider;
    this.authenticationFilter = authenticationFilter;
    this.oAuth2AuthenticationSuccessHandler = oAuth2AuthenticationSuccessHandler;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(requests -> requests.requestMatchers("/", "index", "/css/*", "/js/*").permitAll().requestMatchers("/departments/**").hasAnyRole("ADMIN").requestMatchers("/employees/").hasAnyRole("ADMIN", "EMPLOYEE").anyRequest().authenticated()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).oauth2Login(oauth2 -> oauth2.successHandler(oAuth2AuthenticationSuccessHandler)).exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(authenticationEntryPoint())).addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return (request, response, authException) -> {
      log.warn("Authentication failed: {}", authException.getMessage(), authException);

      ExceptionResponse errorResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, authException.getMessage());
      response.setContentType("application/json");
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    };
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000")); // Adjust as needed
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    return new ProviderManager(loginAuthenticationProvider, jwtAuthenticationProvider);
  }
}