package com.example.pricetag.responses;

import com.example.pricetag.entity.Address;
import com.example.pricetag.enums.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String name;
    private Address address;
    private String email;
    private Long phoneNumber;
    private AppUserRole appUserRole;
    private Date createdAt;
    private Date updatedAt;
}
