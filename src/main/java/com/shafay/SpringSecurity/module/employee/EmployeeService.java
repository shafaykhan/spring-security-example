package com.shafay.SpringSecurity.module.employee;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

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
  public Employee createEmployeeIfNotExists(OidcUser user) {
    Optional<Employee> optionalEmployee = repository.findByEmail(user.getEmail());
    if (optionalEmployee.isPresent()) return optionalEmployee.get();

    Employee employee = Employee.builder()
        .name(user.getFullName())
        .username(user.getFullName().split(" ")[0])
        .email(user.getEmail())
        .password(passwordEncoder.encode("Pass@123"))
        .build();
    return repository.save(employee);
  }
}
