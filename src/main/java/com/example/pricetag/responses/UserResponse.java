package com.example.pricetag.responses;

import com.example.pricetag.entity.CartItem;
import com.example.pricetag.enums.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

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
    private List<CartItem> cartItems;
    private Date createdAt;
    private Date updatedAt;
}
