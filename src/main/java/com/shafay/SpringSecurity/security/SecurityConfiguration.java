package com.shafay.SpringSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  DataSource dataSource;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
            .antMatchers("/", "index", "/css/*", "/js/*").permitAll() // disable urls
            .antMatchers("/api/v1/employees/**").hasAnyRole("USER", "ADMIN")
            .antMatchers("/**").hasRole("ADMIN")
            .and().formLogin();
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select name, password, status = 'ACTIVE'\n" +
                    "from user\n" +
                    "where name = ?")
            .authoritiesByUsernameQuery("select name, role\n" +
                    "from user\n" +
                    "where name = ?");
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
