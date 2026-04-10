package com.example.pricetag.features.subcategory.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubCategoryRequestDto {
    private String subCategoryName;

    private MultipartFile file;
}
