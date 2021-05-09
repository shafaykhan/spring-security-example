package com.shafay.SpringSecurity.module.employee;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

  private List<Employee> employees = Arrays.asList(
          new Employee(1L, "Shafay", "Aurangabad", 1L),
          new Employee(2L, "Juned", "Pune", 1L),
          new Employee(3L, "Sohel", "Beed", 5L),
          new Employee(4L, "Azhar", "Aurangabad", 3L),
          new Employee(5L, "Adil", "Aurangabad", 2L));

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

