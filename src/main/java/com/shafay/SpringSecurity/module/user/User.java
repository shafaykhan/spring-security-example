package com.shafay.SpringSecurity.module.user;

import com.shafay.SpringSecurity.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

  @Id
  private Long id;
  private String name;
  private String password;
  private String role;
  private Status status;
}
