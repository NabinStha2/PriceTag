package com.example.pricetag.dto;

import com.example.pricetag.entity.User;
import com.example.pricetag.entity.Variants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {

    private Long id;

    private User user;

    private ProductDto product;

    private Integer quantity;

    private BigDecimal checkoutAmt;

    private Variants variants;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
