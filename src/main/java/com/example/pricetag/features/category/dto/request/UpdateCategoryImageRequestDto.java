package com.example.pricetag.features.category.dto.request;


import com.example.pricetag.enums.ImageType;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class UpdateCategoryImageRequestDto {
    @Nonnull
    private Long entityId;

    @NotNull
    private ImageType imageType;

    @NotNull
    private MultipartFile file;

    private String entityName;
}
