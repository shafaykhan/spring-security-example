package com.shafay.SpringSecurity.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll() // disable security urls
            .antMatchers("/api/v1/employees").hasAnyRole("USER", "ADMIN")
            .antMatchers("/**").hasRole("ADMIN")
            .and().formLogin();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
            .withUser("user")
            .password("{noop}user")
            .roles("USER")
            .and()
            .withUser("admin")
            .password("{noop}admin")
            .roles("ADMIN");
  }
}
