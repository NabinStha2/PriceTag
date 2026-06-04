package com.example.pricetag.features.product.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VariantResponseDto {
    private Long id;
    private String sku;
    private Integer stockQuantity;
    private BigDecimal actualPrice;
    private BigDecimal discountedPrice;
    private Integer weightInGrams;
    private ColorResponseDto color;
    private SizeResponseDto size;
}
