package com.example.pricetag.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
  private String token;

  private long expiresIn;

  @Override
  public String toString() {
    return "LoginResponse{" +
        "token='" + token + '\'' +
        ", expiresIn=" + expiresIn +
        '}';
  }
}