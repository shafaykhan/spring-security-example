package com.shafay.SpringSecurity.security;

import com.shafay.SpringSecurity.module.user.User;
import com.shafay.SpringSecurity.module.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    Optional<User> userOptional = userRepository.findFirstByName(username);
    userOptional.orElseThrow(() -> new UsernameNotFoundException("User name : " + username +" not found!"));
    return userOptional.map(MyUserDetails::new).get();
  }
}
