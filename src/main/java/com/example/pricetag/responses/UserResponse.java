package com.example.pricetag.responses;

import java.util.Date;

import com.example.pricetag.enums.AppUserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private Long id;
  private String name;
  private String email;
  private Long phoneNumber;
  private AppUserRole appUserRole;
  private Date createdAt;
  private Date updatedAt;
}
