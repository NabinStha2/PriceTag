package com.example.pricetag.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
  @Email
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotEmpty(message = "Password cannot be empty")
  private String password;
}