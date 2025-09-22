package com.shafay.SpringSecurity.module.employee;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

  @Id
  private Long id;
  private String name;
  private String email;
  private String username;
  private String password;
}
