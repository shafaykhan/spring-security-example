package com.shafay.SpringSecurity.module.department;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

  private List<Department> departments = Arrays.asList(
          new Department(1L, "IT"),
          new Department(2L, "Accounting"),
          new Department(3L, "Marketing"),
          new Department(4L, "HR"),
          new Department(5L, "General"));

  @GetMapping
  public ResponseEntity<List<Department>> findAll() {
    return ResponseEntity.ok(departments);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Department> findById(@PathVariable Long id) {
    Department department = departments.stream()
            .filter(dep -> dep.getId().equals(id))
            .findFirst().orElse(null);
    return ResponseEntity.ok(department);
  }
}
