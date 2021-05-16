package com.shafay.SpringSecurity.module.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/identity/users")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable Long id) {
    return ResponseEntity.ok(service.findById(id));
  }

  @PostMapping
  public ResponseEntity<User> save(@RequestBody User user) {
    return ResponseEntity.ok(service.save(user));
  }

  @PutMapping
  public ResponseEntity<User> update(@RequestBody User user) {
    return ResponseEntity.ok(service.update(user));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    service.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
