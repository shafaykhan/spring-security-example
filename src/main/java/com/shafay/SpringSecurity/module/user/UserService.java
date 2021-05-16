package com.shafay.SpringSecurity.module.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  public User save(User user) {
    return repository.save(user);
  }

  public User update(User user) {
    return repository.save(user);
  }

  public List<User> findAll() {
    return repository.findAll();
  }

  public User findById(Long id) {
    return repository.findById(id).orElse(null);
  }

  public void delete(Long id) {
    repository.deleteById(id);
  }

}
