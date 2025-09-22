package com.shafay.SpringSecurity.module.employee;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

  List<Employee> findAll();

  Optional<Employee> findByUsername(String username);
}
