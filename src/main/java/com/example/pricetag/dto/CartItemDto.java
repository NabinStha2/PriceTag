package com.example.pricetag.dto;

import com.example.pricetag.entity.User;
import com.example.pricetag.entity.Variants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;

    private User user;

    private ProductDto product;

    private Long quantity;

    private Double checkoutAmt;

    private Variants variants;

    private Date createdAt;

    private Date updatedAt;
}
