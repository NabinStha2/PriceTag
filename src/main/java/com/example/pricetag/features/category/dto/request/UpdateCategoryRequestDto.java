package com.example.pricetag.features.category.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryRequestDto {
    private Long id;

    @Size(min = 2,
            max = 100,
            message = "Category name must be between 2 and 100 characters")
    private String categoryName;
}
