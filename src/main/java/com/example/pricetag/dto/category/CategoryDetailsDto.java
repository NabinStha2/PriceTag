package com.example.pricetag.dto.category;

import com.example.pricetag.dto.SubCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDetailsDto {
    private Long id;
    private String name;
    private List<SubCategoryDto> subCategoryDtoList = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
