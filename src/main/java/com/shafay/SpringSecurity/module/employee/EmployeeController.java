package com.shafay.SpringSecurity.module.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

  private List<Employee> employees = Arrays.asList(
          new Employee(1L, "User1", "City1", 1L),
          new Employee(2L, "User2", "City2", 1L),
          new Employee(3L, "User3", "City3", 5L),
          new Employee(4L, "User4", "City1", 3L),
          new Employee(5L, "User5", "City1", 2L));

  @GetMapping
  public ResponseEntity<List<Employee>> findAll() {
    return ResponseEntity.ok(employees);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Employee> findById(@PathVariable Long id) {
    Employee employee = employees.stream()
            .filter(emp -> emp.getId().equals(id))
            .findFirst().orElse(null);
    return ResponseEntity.ok(employee);
  }
}

