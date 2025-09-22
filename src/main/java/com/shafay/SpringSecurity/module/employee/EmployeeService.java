package com.shafay.SpringSecurity.module.employee;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService implements UserDetailsService {

  private final EmployeeRepository repository;
  private final PasswordEncoder passwordEncoder;

  public EmployeeService(EmployeeRepository repository, @Lazy PasswordEncoder passwordEncoder) {
    this.repository = repository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<Employee> findAll() {
    return repository.findAll();
  }

  public Employee findById(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new RuntimeException("Employee not found!"));
  }

  public Employee findByUsername(String username) {
    return repository.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Employee not found!"));
  }

  @Transactional
  public Employee createEmployeeIfNotExists(Jwt jwt) {
    Employee employee = Employee.builder()
        .name(jwt.getClaim("name"))
        .username(jwt.getClaim("name").toString().split(" ")[0])
        .email(jwt.getClaim("email"))
        .password(passwordEncoder.encode("Pass@123"))
        .build();
    return repository.save(employee);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Employee employee = findByUsername(username);
    return User.builder()
        .username(employee.getUsername())
        .password(employee.getPassword())
        .authorities("EMPLOYEE")
        .build();
  }
}
