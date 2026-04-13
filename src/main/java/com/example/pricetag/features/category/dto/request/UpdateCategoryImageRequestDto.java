package com.example.pricetag.features.category.dto.request;


import com.example.pricetag.enums.ImageType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCategoryImageRequestDto {
    @NotNull
    private Long entityId;

    @NotNull
    private ImageType imageType;

    @NotNull
    private MultipartFile file;

    private String entityName;
}
